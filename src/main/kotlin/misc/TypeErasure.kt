package misc

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.collections.all
import kotlin.collections.forEach
import kotlin.collections.getOrPut
import kotlin.jvm.javaClass

object TypeErasure {
    // Demonstrating type erasure
    fun <T> getListSize(list: List<T>): Int {
        return list.size
    }

    fun demonstrateBasicErasure() {
        val stringList = listOf("apple", "banana", "cherry")
        val intList = listOf(1, 2, 3, 4)

        println("Size of string list: ${getListSize(stringList)}")
        println("Size of integer list: ${getListSize(intList)}")

        // This demonstrates type erasure in action
        println("Runtime type of stringList: ${stringList.javaClass}")
        println("Runtime type of intList: ${intList.javaClass}")
    }

    // Showing type checking limitations due to erasure
    fun demonstrateTypeCheckingLimitations() {
        val stringList = listOf("apple", "banana", "cherry")
        val intList = listOf(1, 2, 3, 4)
        val anyList: List<Any> = listOf("mixed", 123, true)

        // Type checking is limited to the raw type (List), not its type parameter
        if (stringList is List<*>) {
            println("stringList is a List of something (we can't check what exactly)")
        }

        // We can check elements individually
        if (stringList.all { it is String }) {
            println("All elements in stringList are Strings")
        }

        // Type casting with generics can lead to runtime exceptions if not careful
        val uncheckedCast = anyList as List<String>  // No warning at compile time!

        try {
            // This will fail at runtime with ClassCastException
            val problematicItem: String = uncheckedCast[1]  // Item at index 1 is an Int, not a String
            println("This won't print due to ClassCastException")
        } catch (e: ClassCastException) {
            println("ClassCastException caught: ${e.message}")
        }
    }

    // Using reified type parameters
    inline fun <reified T> List<*>.hasElementsOfType(): Boolean {
        return this.all { it is T }
    }

    fun demonstrateReifiedTypeParameters() {
        val stringList = listOf("apple", "banana", "cherry")
        val intList = listOf(1, 2, 3, 4)
        val mixedList = listOf("string", 123, true)

        println("stringList has all String elements: ${stringList.hasElementsOfType<String>()}")
        println("intList has all Int elements: ${intList.hasElementsOfType<Int>()}")
        println("mixedList has all String elements: ${mixedList.hasElementsOfType<String>()}")

        // Using our custom filterIsInstance function
        val filteredStrings = mixedList.filterIsInstance<String>()
        println("Filtered strings from mixed list: $filteredStrings")

        // Using Kotlin's built-in filterIsInstance
        val builtInFiltered = mixedList.filterIsInstance<Int>()
        println("Built-in filtered integers from mixed list: $builtInFiltered")
    }

    // Another example of reified type parameters for filtering
    inline fun <reified T> List<*>.filterIsInstance(): List<T> {
        val result = mutableListOf<T>()
        for (element in this) {
            if (element is T) {
                @Suppress("UNCHECKED_CAST")
                result.add(element as T)
            }
        }
        return result
    }

    // Creating a type-safe repository using type tokens

    // Our domain entities
    class User(val id: Int, val name: String) {
        override fun toString() = "User(id=$id, name='$name')"
    }

    class Product(val id: Int, val name: String, val price: Double) {
        override fun toString() = "Product(id=$id, name='$name', price=$price)"
    }

    // Type token to preserve generic type information
    abstract class TypeToken<T> {
        // This property will give us access to the generic type at runtime
        val type: Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is TypeToken<*>) return false
            return type == other.type
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    // Repository that maintains type safety using type tokens
    class Repository {
        private val storage = mutableMapOf<Type, MutableList<Any>>()

        // Store an item with its type
        fun <T : Any> store(item: T, typeToken: TypeToken<T>) {
            val list = storage.getOrPut(typeToken.type) { mutableListOf() }
            list.add(item)
        }

        // Retrieve all items of a specific type
        fun <T : Any> findAll(typeToken: TypeToken<T>): List<T> {
            val list = storage[typeToken.type] ?: return emptyList()
            @Suppress("UNCHECKED_CAST")
            return list as List<T>
        }
    }

    fun demonstrateTypeTokens() {
        val repository = Repository()

        // Create type tokens for our types
        val userType = object : TypeToken<User>() {}
        val productType = object : TypeToken<Product>() {}

        // Store different types of objects
        repository.store(User(1, "Alice"), userType)
        repository.store(User(2, "Bob"), userType)
        repository.store(Product(1, "Laptop", 999.99), productType)
        repository.store(Product(2, "Phone", 599.99), productType)

        // Retrieve all users
        val users = repository.findAll(userType)
        println("Users:")
        users.forEach { println(it) }

        // Retrieve all products
        val products = repository.findAll(productType)
        println("\nProducts:")
        products.forEach { println(it) }
    }


    @JvmStatic
    fun main(args: Array<String>) {
        println("=== Basic Type Erasure Demonstration ===")
        demonstrateBasicErasure()

        println("\n=== Type Checking Limitations ===")
        demonstrateTypeCheckingLimitations()

        println("\n=== Reified Type Parameters ===")
        demonstrateReifiedTypeParameters()

        println("\n=== Type Tokens ===")
        demonstrateTypeTokens()
    }
}
