package functions

import kotlin.collections.filter
import kotlin.collections.groupBy
import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.collections.none
import kotlin.collections.withIndex
import kotlin.text.isNotEmpty
import kotlin.text.isNullOrBlank
import kotlin.text.matches
import kotlin.text.split
import kotlin.text.substring
import kotlin.text.toRegex
import kotlin.text.uppercase

object Extensions {

    // Basic String extensions
    fun String.isValidEmail(): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        return this.matches(emailRegex.toRegex())
    }

    fun String.truncate(maxLength: Int): String {
        return if (this.length <= maxLength) {
            this
        } else {
            this.substring(0, maxLength - 3) + "..."
        }
    }

    val String.initials: String
        get() {
            return this.split(" ")
                .filter { it.isNotEmpty() }
                .joinToString("") { it[0].uppercase() }
        }

    // Extensions for nullable types
    fun String?.safeLength(): Int {
        return this?.length ?: 0
    }

    fun String?.orDefault(default: String): String {
        return if (this.isNullOrBlank()) default else this
    }

    // Collection extensions
    fun <T> List<T>.secondOrNull(): T? {
        return if (this.size >= 2) this[1] else null
    }

    fun <T> List<T>.chunked(size: Int): List<List<T>> {
        require(size > 0) { "Chunk size must be positive" }

        return this.withIndex()
            .groupBy { it.index / size }
            .map { entry -> entry.value.map { it.value } }
    }

    fun <T, R> Iterable<T>.mapIfNotEmpty(transform: (T) -> R): List<R> {
        return if (this.none()) {
            emptyList()
        } else {
            this.map(transform)
        }
    }

    // Extensions with receivers
    data class Person(val firstName: String, val lastName: String, val age: Int)

    fun Person.fullName(): String {
        return "$firstName $lastName"
    }

    fun Person.isAdult(adultAge: Int = 18): Boolean {
        return age >= adultAge
    }

    fun Person.applyToFullName(block: String.() -> String): String {
        val name = this.fullName()
        return name.block()
    }

    @JvmStatic
    fun main(args: Array<String>) {

        val email = "user@example.com"
        val longText = "This is a very long string that needs to be truncated for display purposes."
        val nullableString: String? = null
        val personName = "John Smith"
        val people = listOf(
            Person("Alice", "Johnson", 24),
            Person("Bob", "Smith", 17),
            Person("Charlie", "Williams", 32)
        )
        val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        println("Is '$email' a valid email? ${email.isValidEmail()}")
        println("Truncated text: ${longText.truncate(30)}")
        println("Initials of '$personName': ${personName.initials}")

        println("Length of null string: ${nullableString.safeLength()}")
        println("Null string with default: ${nullableString.orDefault("Default Value")}")

        println("Second element: ${numbers.secondOrNull()}")
        println("Numbers in chunks of 3: ${numbers.chunked(3)}")

        val alice = people[0]
        println("${alice.fullName()} is an adult: ${alice.isAdult()}")

        val formattedName = alice.applyToFullName {
            this.uppercase()
        }
        println("Formatted name: $formattedName")

        val adults = people.filter { it.isAdult() }
        val adultNames = adults.mapIfNotEmpty { it.fullName() }
        println("Adult names: $adultNames")
    }
}
