package collections

import kotlin.collections.filter
import kotlin.collections.filterNotNull
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.orEmpty
import kotlin.text.contains
import kotlin.text.isEmpty

object NullSafety {
    // User data class with potentially missing fields
    data class User(
        val id: Int,
        val name: String,
        val email: String?,  // Email might be missing
        val phone: String?,  // Phone might be missing
        val isActive: Boolean = true
    )

    // Repository class that works with nullable collections
    class UserRepository {

        private val users = listOf(
            User(1, "Alice Smith", "alice@example.com", "555-123-4567"),
            User(2, "Bob Johnson", null, "555-234-5678"),
            User(3, "Carol Williams", "carol@example.com", null),
            User(4, "Dave Brown", null, null),
            User(5, "Eve Davis", "eve@example.com", "555-345-6789")
        )

        // This method returns a nullable list (the list itself might be null)
        fun findUsers(query: String): List<User>? {
            // Simulate a database failure for certain queries
            if (query.isEmpty()) {
                return null
            }

            // Return users that match the query by name
            return users.filter { it.name.contains(query, ignoreCase = true) }
        }

        // This method returns a list of nullable strings (the elements might be null)
        fun getAllEmails(): List<String?> {
            return users.map { it.email }  // Some emails might be null
        }

        // This method returns a nullable list of nullable strings
        fun findEmails(query: String): List<String?>? {
            val matchingUsers = findUsers(query)
            // If findUsers returned null, this will also return null
            return matchingUsers?.map { it.email }
        }
    }

    // Service class that safely handles nullable collections
    class UserService(private val repository: UserRepository) {

        // Safely get count of matching users
        fun countMatchingUsers(query: String): Int {
            // Use the safe call with Elvis operator to handle potential null
            return repository.findUsers(query)?.size ?: 0
        }

        // Safely print information about matching users
        fun printUserInfo(query: String) {
            val matchingUsers = repository.findUsers(query)

            // Handle the case where the entire list might be null
            if (matchingUsers == null) {
                println("No data available for query: $query")
                return
            }

            // Use a check to leverage Kotlin's smart cast
            if (matchingUsers.isEmpty()) {
                println("No users found matching: $query")
                return
            }

            // At this point, matchingUsers is non-null and non-empty
            println("Found ${matchingUsers.size} users:")
            for (user in matchingUsers) {
                // Safe handling of nullable fields with Elvis operator
                val emailInfo = user.email ?: "Email not provided"
                val phoneInfo = user.phone ?: "Phone not provided"

                println("${user.name}: $emailInfo, $phoneInfo")
            }
        }

        // Get all valid (non-null) email addresses
        fun getValidEmails(): List<String> {
            // Convert the list of nullable emails to a list of non-null emails
            return repository.getAllEmails().filterNotNull()
        }

        // Safely process emails for a query
        fun processEmails(query: String): List<String> {
            // Handle the nullable list of nullable elements
            val emails = repository.findEmails(query)

            // First, use orEmpty to handle the case where the list itself is null
            // Then, use filterNotNull to remove any null elements
            return emails.orEmpty().filterNotNull()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val repository = UserRepository()
        val service = UserService(repository)

        // Example 1: Safely handling count
        println("Count of users with 'a' in name: ${service.countMatchingUsers("a")}")
        println("Count of users with 'z' in name: ${service.countMatchingUsers("z")}")

        // Example 2: Handling empty query (which returns null from repository)
        println("Count of users with empty query: ${service.countMatchingUsers("")}")

        // Example 3: Print user information with null handling
        println("\n--- Users with 'a' in name ---")
        service.printUserInfo("a")

        println("\n--- Users with 'z' in name ---")
        service.printUserInfo("z")

        println("\n--- Empty query (repository returns null) ---")
        service.printUserInfo("")

        // Example 4: Filter out null emails
        val validEmails = service.getValidEmails()
        println("\n--- Valid Emails (${validEmails.size}) ---")
        validEmails.forEach { println(it) }

        // Example 5: Process emails (handles both nullable list and nullable elements)
        println("\n--- Processed Emails for 'a' ---")
        val processedEmails = service.processEmails("a")
        if (processedEmails.isEmpty()) {
            println("No valid emails found")
        } else {
            processedEmails.forEach { println(it) }
        }

        println("\n--- Processed Emails for empty query (null list) ---")
        val emptyResult = service.processEmails("")
        if (emptyResult.isEmpty()) {
            println("No valid emails found")
        } else {
            emptyResult.forEach { println(it) }
        }
    }
}
