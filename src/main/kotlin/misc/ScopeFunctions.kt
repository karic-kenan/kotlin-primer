package misc

import kotlin.also
import kotlin.apply
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.set
import kotlin.let
import kotlin.run

object ScopeFunctions {

    // Define a simple User class
    data class User(
        var name: String = "",
        var email: String = "",
        var age: Int = 0,
        var isVerified: Boolean = false,
        var preferences: MutableMap<String, Any> = mutableMapOf()
    )

    // Using apply to configure a new user
    fun createUser(): User {
        // Traditional way (without scope functions)
//        val user = User()
//        user.name = "John Doe"
//        user.email = "john.doe@example.com"
//        user.age = 28
//        user.isVerified = true
//        user.preferences["theme"] = "dark"
//        return user

        // Using apply
        return User().apply {
            name = "John Doe"
            email = "john.doe@example.com"
            age = 28
            isVerified = true
            preferences["theme"] = "dark"
        }
    }

    // Using also for logging without disrupting the chain
    fun processUser(user: User): User {
        return user.also {
            println("Processing user: ${it.name}")
            println("Email: ${it.email}")
        }
    }

    // Using let with nullable values
    fun findUserByEmail(email: String?): User? {
        // Simulating database lookup
        return if (email == "john.doe@example.com") {
            User(name = "John Doe", email = email)
        } else {
            null
        }
    }

    fun processUserSafely(email: String?) {
        val user = findUserByEmail(email)

        // Without let, we would need a null check
        if (user != null) {
            println("User found: ${user.name}")
            // process user...
        }

        // With let and safe call
        user?.let {
            println("User found with let: ${it.name}")
            // process user...
        }
    }

    // Using run for computations
    fun calculateUserTier(user: User): String {
        return user.run {
            val hasPreferredTheme = preferences.containsKey("theme")
            val ageGroup = when {
                age < 18 -> "junior"
                age < 65 -> "adult"
                else -> "senior"
            }

            // Determine tier based on user properties
            when {
                isVerified && age > 30 && hasPreferredTheme -> "PLATINUM"
                isVerified && hasPreferredTheme -> "GOLD"
                isVerified -> "SILVER"
                else -> "BRONZE"
            }
        }
    }

    // Using with for accessing object members
    fun generateUserReport(user: User): String {
        return with(user) {
            val verificationStatus = if (isVerified) "verified" else "unverified"
            val ageStatus = "age: $age"

            // Using string builder to create a report
            kotlin.text.StringBuilder().apply {
                append("User Report for $name\n")
                append("Email: $email ($verificationStatus)\n")
                append("Age: $age\n")
                append("Preferences: ${preferences.size} items\n")

                if (preferences.isNotEmpty()) {
                    append("Details:\n")
                    preferences.forEach { (key, value) ->
                        append("  - $key: $value\n")
                    }
                }
            }.toString()
        }
    }

    // Combining multiple scope functions
    fun updateUserPreferences(user: User, theme: String?, language: String?): User {
        return user.apply {
            // Set default language if none exists
            if (!preferences.containsKey("language")) {
                preferences["language"] = "English"
            }
        }.also { updatedUser ->
            println("Updating preferences for user: ${updatedUser.name}")

            theme?.let { themeValue ->
                updatedUser.preferences["theme"] = themeValue
                println("Theme set to: $themeValue")
            }

            language?.let { langValue ->
                updatedUser.preferences["language"] = langValue
                println("Language set to: $langValue")
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {

        // Create and configure a user
        val user = createUser()
        println("User created: ${user.name}")

        // Process the user with side effects
        val processedUser = processUser(user)
        println("User processed successfully")

        // Demonstrate null safety with let
        processUserSafely("john.doe@example.com")
        processUserSafely("nonexistent@example.com")

        // Calculate user tier
        val tier = calculateUserTier(user)
        println("User tier: $tier")

        // Generate user report
        val report = generateUserReport(user)
        println("\nREPORT:\n$report")

        // Update user preferences
        val updatedUser = updateUserPreferences(user, "light", null)
        println("Updated user preferences: ${updatedUser.preferences}")
    }
}
