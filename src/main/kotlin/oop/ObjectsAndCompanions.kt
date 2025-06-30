package oop

import kotlin.collections.joinToString
import kotlin.text.isNotEmpty
import kotlin.text.matches
import kotlin.text.toRegex
import kotlin.to

object ObjectsAndCompanions {

    object UserAuthManager {
        private var currentUser: User? = null
        private var authToken: String? = null

        fun login(username: String, password: String): Boolean {
            return if (isValidCredentials(username, password)) {
                currentUser = User(username, "$username@example.com")
                authToken = "token_${username}_${System.currentTimeMillis()}"
                println("User $username logged in successfully")
                true
            } else {
                currentUser = null
                authToken = null
                println("Login failed for user $username")
                false
            }
        }

        fun logout() {
            val username = currentUser?.name ?: "Unknown"
            currentUser = null
            authToken = null
            println("User $username logged out")
        }

        fun isAuthenticated(): Boolean {
            return currentUser != null && authToken != null
        }

        private fun isValidCredentials(username: String, password: String): Boolean {
            // In a real app, you would check against a database or server
            return username.isNotEmpty() && password.length >= 6
        }

        fun getCurrentUser(): User? {
            return currentUser
        }
    }

    class User(
        val name: String,
        val email: String,
        val userType: UserType = UserType.STANDARD
    ) {
        companion object {
            const val MIN_USERNAME_LENGTH = 3
            const val MAX_USERNAME_LENGTH = 50
            private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"

            fun validateEmail(email: String): Boolean {
                return email.matches(EMAIL_REGEX.toRegex())
            }

            fun validateUsername(username: String): Boolean {
                return username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH
            }

            fun createStandardUser(name: String, email: String): User? {
                return if (validateUsername(name) && validateEmail(email)) {
                    User(name, email, UserType.STANDARD)
                } else {
                    println("Invalid user data provided")
                    null
                }
            }

            fun createAdminUser(name: String, email: String): User? {
                return if (validateUsername(name) && validateEmail(email)) {
                    User(name, email, UserType.ADMIN)
                } else {
                    println("Invalid admin user data provided")
                    null
                }
            }

            fun createGuestUser(): User {
                return User("Guest", "guest@example.com", UserType.GUEST)
            }
        }

        fun displayInfo() {
            println("User: $name, Email: $email, Type: $userType")
        }
    }

    enum class UserType {
        GUEST,
        STANDARD,
        PREMIUM,
        ADMIN
    }

    object UserPreferences {
        var theme: String = "light"
        var notificationsEnabled: Boolean = true
        var fontSize: Int = 14

        fun savePreferences() {
            println("Saving preferences: Theme=$theme, Notifications=$notificationsEnabled, Font Size=$fontSize")
            // In a real app, this would save to SharedPreferences, a database, or a server
        }

        fun resetToDefaults() {
            theme = "light"
            notificationsEnabled = true
            fontSize = 14
            println("Preferences reset to defaults")
        }

        fun displayPreferences() {
            println("Current Preferences:")
            println("- Theme: $theme")
            println("- Notifications: $notificationsEnabled")
            println("- Font Size: $fontSize")
        }
    }

    interface AnalyticsService {
        fun trackEvent(name: String, properties: Map<String, Any> = emptyMap())
        fun trackScreen(screenName: String)
    }

    object AppAnalytics : AnalyticsService {
        override fun trackEvent(name: String, properties: Map<String, Any>) {
            val propsString = properties.entries.joinToString { "${it.key}=${it.value}" }
            println("Analytics Event: $name, Properties: $propsString")
        }

        override fun trackScreen(screenName: String) {
            println("Screen View: $screenName")
        }

        fun trackLogin(username: String, successful: Boolean) {
            trackEvent(
                "user_login",
                mapOf(
                    "username" to username,
                    "successful" to successful,
                    "timestamp" to System.currentTimeMillis()
                )
            )
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // Create a user using companion object factory methods
        val user = User.createStandardUser("JohnDoe", "john@example.com")

        if (user != null) {
            user.displayInfo()
        }

        // Use companion object utility methods
        println("Email validation: ${User.validateEmail("valid@example.com")}")
        println("Email validation: ${User.validateEmail("invalid-email")}")

        // Use the UserAuthManager singleton
        UserAuthManager.login("JohnDoe", "password123")
        println("Is authenticated: ${UserAuthManager.isAuthenticated()}")

        // Track login with analytics
        AppAnalytics.trackLogin("JohnDoe", true)

        // Use UserPreferences singleton
        UserPreferences.displayPreferences()
        UserPreferences.theme = "dark"
        UserPreferences.notificationsEnabled = false
        UserPreferences.displayPreferences()
        UserPreferences.savePreferences()

        // Create different user types
        val adminUser = User.createAdminUser("AdminUser", "admin@example.com")
        val guestUser = User.createGuestUser()

        adminUser?.displayInfo()
        guestUser.displayInfo()

        // Log out
        UserAuthManager.logout()
        println("Is authenticated after logout: ${UserAuthManager.isAuthenticated()}")

        // Track screen view
        AppAnalytics.trackScreen("UserProfileScreen")
    }
}
