package oop

import kotlin.collections.forEach
import kotlin.collections.indices
import kotlin.collections.joinToString
import kotlin.collections.set
import kotlin.collections.toList
import kotlin.let

object Generics {

    // A basic generic container
    class Box<T>(var content: T) {

        // Check if the box is empty (content is null)
        fun isEmpty(): Boolean {
            return content == null
        }

        // Get the content with its type information
        fun retrieve(): T {
            return content
        }

        // Replace the content with a new value
        fun put(newContent: T) {
            content = newContent
        }

        // Print information about the content
        fun describe() {
            println("Box contains a ${content?.let { it::class.simpleName }} with value: $content")
        }
    }

    // Demonstrating the use of our generic Box
    fun demonstrateGenericBox() {
        // Create a Box containing a String
        val stringBox = Box<String>("Hello, Generics!")
        stringBox.describe()

        // Create a Box containing an Integer
        val intBox = Box(42)  // Type inference determines this is Box<Int>
        intBox.describe()

        // Retrieve and use the content with correct type
        val greeting: String = stringBox.retrieve()
        println("String length: ${greeting.length}")

        val number: Int = intBox.retrieve()
        println("Number plus 10: ${number + 10}")

        // Replace content
        stringBox.put("Updated content")
        intBox.put(99)

        // Verify the changes
        stringBox.describe()
        intBox.describe()
    }

    // Generic methods
    fun <T> boxOf(value: T): Box<T> {
        return Box(value)
    }

    // Generic function to swap elements in an array
    fun <T> Array<T>.swap(index1: Int, index2: Int): Array<T> {
        // Check if indices are valid
        require(index1 >= 0 && index1 < size && index2 >= 0 && index2 < size) {
            "Invalid indices: $index1 and/or $index2 for array of size $size"
        }

        // Perform the swap
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp

        return this
    }

    // Generic interface with type constraint
    interface Repository<T : Any> {
        fun getById(id: Int): T?
        fun getAll(): List<T>
        fun save(item: T): Boolean
        fun update(id: Int, item: T): Boolean
        fun delete(id: Int): Boolean
    }

    // First, let's create a simple User class
    data class User(
        val id: Int,
        val name: String,
        val email: String
    )

    // Implementing the Repository interface for the User class
    class UserRepository : Repository<User> {
        private val users = mutableMapOf<Int, User>()

        override fun getById(id: Int): User? {
            return users[id]
        }

        override fun getAll(): List<User> {
            return users.values.toList()
        }

        override fun save(item: User): Boolean {
            // Don't overwrite existing users
            if (users.containsKey(item.id)) {
                return false
            }
            users[item.id] = item
            return true
        }

        override fun update(id: Int, item: User): Boolean {
            // Ensure the IDs match
            if (item.id != id) {
                return false
            }
            // Only update if the user exists
            if (!users.containsKey(id)) {
                return false
            }
            users[id] = item
            return true
        }

        override fun delete(id: Int): Boolean {
            return users.remove(id) != null
        }
    }

    // Generic class with multiple type parameters
    class KeyValueStore<K, V> {
        private val store = mutableMapOf<K, V>()

        fun put(key: K, value: V) {
            store[key] = value
        }

        fun get(key: K): V? {
            return store[key]
        }

        fun containsKey(key: K): Boolean {
            return store.containsKey(key)
        }

        fun remove(key: K): V? {
            return store.remove(key)
        }

        fun clear() {
            store.clear()
        }

        fun size(): Int {
            return store.size
        }

        fun keys(): Set<K> {
            return store.keys
        }

        fun values(): Collection<V> {
            return store.values
        }
    }

    // First, let's set up a simple class hierarchy
    open class Fruit(val name: String) {
        fun wash() = println("Washing $name")
    }

    class Apple : Fruit("Apple") {
        fun removeSeeds() = println("Removing seeds from apple")
    }

    class Orange : Fruit("Orange") {
        fun peel() = println("Peeling orange")
    }

    // Now let's create classes that demonstrate variance
    class FruitBasket<out T : Fruit>(private val fruits: List<T>) {
        // Returns the fruit at the specified index
        fun getAt(index: Int): T {
            require(index in fruits.indices) { "Invalid index" }
            return fruits[index]
        }

        // Returns the size of the basket
        fun size(): Int = fruits.size

        // Note: We can't add fruits because T is marked as 'out'
        // This wouldn't compile:
        // fun add(fruit: T) { ... }
    }

    class FruitProcessor<in T : Fruit> {
        // Process a fruit
        fun process(fruit: T) {
            fruit.wash()
            println("Processing ${fruit.name}")
        }

        // Note: We can't return T because it's marked as 'in'
        // This wouldn't compile:
        // fun produceFruit(): T { ... }
    }

    @JvmStatic
    fun main(args: Array<String>) {

        println("=== Demonstrating Generic Box ===")
        demonstrateGenericBox()
        println()

        println("=== Demonstrating Generic Functions ===")
        // Using boxOf function
        val doubleBox = boxOf(3.14)
        doubleBox.describe()

        // Using array swap extension
        val names = arrayOf("Alice", "Bob", "Charlie", "David")
        println("Original array: ${names.joinToString()}")
        names.swap(1, 3)
        println("After swapping indices 1 and 3: ${names.joinToString()}")
        println()

        println("=== Demonstrating Generic Repository ===")
        val userRepo = UserRepository()

        // Adding users
        userRepo.save(User(1, "Alice", "alice@example.com"))
        userRepo.save(User(2, "Bob", "bob@example.com"))
        userRepo.save(User(3, "Charlie", "charlie@example.com"))

        // Fetching users
        println("All users:")
        userRepo.getAll().forEach { println("  ${it.name} (${it.email})") }

        // Getting a specific user
        val user = userRepo.getById(2)
        println("\nUser with ID 2: ${user?.name} (${user?.email})")

        // Updating a user
        userRepo.update(2, User(2, "Bob Smith", "bobsmith@example.com"))
        val updatedUser = userRepo.getById(2)
        println("\nUpdated user with ID 2: ${updatedUser?.name} (${updatedUser?.email})")
        println()

        println("=== Demonstrating Generic KeyValueStore ===")
        // String keys, Int values
        val scores = KeyValueStore<String, Int>()
        scores.put("Alice", 95)
        scores.put("Bob", 87)
        scores.put("Charlie", 92)

        println("Bob's score: ${scores.get("Bob")}")
        println("All students: ${scores.keys().joinToString()}")

        // Int keys, User values
        val userById = KeyValueStore<Int, User>()
        userById.put(1, User(1, "Alice", "alice@example.com"))
        userById.put(2, User(2, "Bob", "bob@example.com"))

        val bobUser = userById.get(2)
        println("User with ID 2: ${bobUser?.name} (${bobUser?.email})")
        println()

        println("=== Demonstrating Variance ===")
        // Create some fruits
        val apple1 = Apple()
        val apple2 = Apple()
        val orange = Orange()

        // Covariance with FruitBasket
        val appleBasket = FruitBasket(listOf(apple1, apple2))
        val orangeBasket = FruitBasket(listOf(orange))

        // We can assign a FruitBasket<Apple> to a FruitBasket<Fruit>
        // This works because FruitBasket is covariant (uses 'out')
        val fruitBasket: FruitBasket<Fruit> = appleBasket
        val someFruit = fruitBasket.getAt(0)
        someFruit.wash()  // We can call Fruit methods

        // Contravariance with FruitProcessor
        // Create a processor for all fruits
        val fruitProcessor = FruitProcessor<Fruit>()

        // We can assign a FruitProcessor<Fruit> to a FruitProcessor<Apple>
        // This works because FruitProcessor is contravariant (uses 'in')
        val appleProcessor: FruitProcessor<Apple> = fruitProcessor

        // Process some fruits
        appleProcessor.process(apple1)  // Works fine
        fruitProcessor.process(orange)  // Also works fine
    }
}
