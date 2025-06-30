package oop

import kotlin.collections.find
import kotlin.collections.forEach
import kotlin.collections.toMutableList
import kotlin.let

object NullSafety {

    // User profile with nullable fields
    open class User(
        val id: String,                  // Every user must have an ID
        val username: String,            // Every user must have a username
        val email: String?,              // Email might not be provided or might be hidden
        val bio: String? = null,         // Bio is optional
        val age: Int? = null,            // Age is optional
        val phoneNumber: String? = null  // Phone number is optional
    )

    // Creating users with different nullable properties
    fun createUsers(): List<User> {
        val minimalUser = User(
            id = "u1",
            username = "john_doe",
            email = "john@example.com",
        )

        val completeUser = User(
            id = "u2",
            username = "jane_smith",
            email = "jane@example.com",
            bio = "Software developer and hiking enthusiast",
            age = 28,
            phoneNumber = "555-123-4567"
        )

        val partialUser = User(
            id = "u3",
            username = "bob_jenkins",
            email = "bob@example.com",
            bio = "Love photography and travel"
        )

        return listOf(minimalUser, completeUser, partialUser)
    }

    // Function to safely display user information
    fun displayUserInfo(user: User) {
        println("ID: ${user.id}")
        println("Username: ${user.username}")

        println("Email: ${user.email ?: "Not provided"}")

        user.bio?.let { bio ->
            println("Bio: $bio")
        } ?: println("Bio: Not provided")

        println("Age: ${user.age?.toString() ?: "Not provided"}")

        println("Phone: ${user.phoneNumber ?: "Not provided"}")
        println("-----------------------")
    }

    // Search function that handles nullability
    fun findUserByEmail(users: List<User>, email: String?): User? {
        return email?.let { searchEmail ->
            users.find { user ->
                user.email == searchEmail
            }
        }
    }

    // Profile formatter with comprehensive null handling
    fun formatUserProfile(user: User): String {
        val profileBuilder = kotlin.text.StringBuilder()
        profileBuilder.append("Profile for ${user.username}:\n")

        user.email?.let { email ->
            profileBuilder.append("Email: $email\n")
        }

        profileBuilder.append("Bio: ${user.bio ?: "No bio provided"}\n")
        profileBuilder.append("Age: ${user.age?.toString() ?: "Not specified"}\n")

        if (user.phoneNumber != null) {
            profileBuilder.append("Phone: ${user.phoneNumber}\n")
        } else {
            profileBuilder.append("Phone: Not provided\n")
        }

        return profileBuilder.toString()
    }

    // Premium user class with additional nullable properties
    class PremiumUser(
        id: String,
        username: String,
        email: String?,
        bio: String? = null,
        age: Int? = null,
        phoneNumber: String? = null,
        val memberSince: String,
        val subscriptionLevel: String,
        val paymentMethod: String? = null
    ) : User(id, username, email, bio, age, phoneNumber) {

        fun getPaymentInfo(): String {
            return "Level: $subscriptionLevel, Payment: ${paymentMethod ?: "Not provided"}"
        }

        fun canAccessMatureContent(): Boolean {
            // Only allow access if age is specified and at least 18
            return age?.let { it >= 18 } ?: false
        }
    }

    // User manager with nullable collections and properties

    class UserManager {
        private var users: MutableList<User>? = null
        private var activeUser: User? = null

        fun initialize(initialUsers: List<User>?) {
            users = initialUsers?.toMutableList()
        }

        fun addUser(user: User): Boolean {
            if (users == null) {
                users = mutableListOf(user)
                return true
            }

            return users?.add(user) ?: false
        }

        fun getActiveUserOrThrow(): User {
            return activeUser ?: throw kotlin.IllegalStateException("No active user set")
        }

        fun getUserEmail(userId: String): String {
            return users?.find { it.id == userId }?.email ?: "Email not available"
        }
    }


    @JvmStatic
    fun main(args: Array<String>) {
        val users = createUsers()

        println("USER INFORMATION:")
        users.forEach { user ->
            displayUserInfo(user)
        }

        println("SEARCH RESULTS:")
        val foundUser = findUserByEmail(users, "jane@example.com")
        println("Found user: ${foundUser?.username ?: "No user found"}")

        val notFoundUser = findUserByEmail(users, "nobody@example.com")
        println("Found user: ${notFoundUser?.username ?: "No user found"}")

        val nullSearchResult = findUserByEmail(users, null)
        println("Found user: ${nullSearchResult?.username ?: "No user found"}")

        println("\nFORMATTED PROFILES:")
        users.forEach { user ->
            println(formatUserProfile(user))
            println("-----------------------")
        }

        println("\nPREMIUM USER FEATURES:")
        val premiumUser = PremiumUser(
            id = "p1",
            username = "premium_alex",
            email = "alex@example.com",
            bio = "Premium user since 2022",
            age = 25,
            memberSince = "2022-03-15",
            subscriptionLevel = "Gold"
        )

        println(formatUserProfile(premiumUser))
        println("Payment Info: ${premiumUser.getPaymentInfo()}")
        println("Can access mature content: ${premiumUser.canAccessMatureContent()}")

        println("\nUSER MANAGER:")
        val userManager = UserManager()

        println("Adding user before initialization: ${userManager.addUser(users[0])}")

        userManager.initialize(users)
        println("User email: ${userManager.getUserEmail("u2")}")
        println("Nonexistent user email: ${userManager.getUserEmail("nobody")}")

        try {
            userManager.getActiveUserOrThrow()
        } catch (e: IllegalStateException) {
            println("Expected exception: ${e.message}")
        }
    }
}
