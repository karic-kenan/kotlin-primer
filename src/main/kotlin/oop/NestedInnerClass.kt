package oop

import kotlin.text.capitalize
import kotlin.text.lowercase

object NestedInnerClass {

    // Main NotificationSystem class
    class NotificationSystem {
        private val systemName = "EcommerceNotifier"

        // Static nested class for configuration
        class Configuration {
            var defaultPriority = "NORMAL"
            var retryCount = 3
            var timeout = 30L  // seconds

            fun displayConfig() {
                println("Priority: $defaultPriority, Retries: $retryCount, Timeout: ${timeout}s")
            }
        }

        // Inner class for Email notifications
        inner class EmailNotification(private val recipient: String) {
            private val subject = "Notification from $systemName"

            fun send(message: String) {
                println("Sending email to $recipient with subject '$subject'")
                println("Message: $message")
            }
        }

        // Inner class for SMS notifications
        inner class SMSNotification(private val phoneNumber: String) {
            fun send(message: String) {
                println("$systemName: Sending SMS to $phoneNumber")
                println("Message: $message")
            }
        }

        // Outer class property with potential name conflict
        private val status = "Active"

        // Inner class with name conflict resolution
        inner class NotificationEvent(private val status: String) {
            fun logEvent() {
                println("Event with status: $status")          // Refers to inner class property
                println("System status: ${this@NotificationSystem.status}")  // Refers to outer class property
            }
        }

        // Private nested class for internal use only
        private class NotificationDatabase {
            fun logNotification(type: String, recipient: String) {
                println("Logging $type notification to $recipient in database")
            }
        }

        // Method to send a notification using our nested classes
        fun sendNotification(type: String, recipient: String, message: String) {
            // Using the private nested class
            val database = NotificationDatabase()

            when (type.lowercase()) {
                "email" -> {
                    val emailNotifier = EmailNotification(recipient)
                    emailNotifier.send(message)
                    database.logNotification("email", recipient)
                }

                "sms" -> {
                    val smsNotifier = SMSNotification(recipient)
                    smsNotifier.send(message)
                    database.logNotification("sms", recipient)
                }

                else -> {
                    println("Unsupported notification type: $type")
                }
            }
        }

        // Nested interface
        interface NotificationFormatter {
            fun format(message: String): String
        }

        // Nested enum class
        enum class Priority {
            LOW, NORMAL, HIGH, URGENT;

            fun getDisplayName(): String {
                return name.lowercase().capitalize()
            }
        }

        // Class implementing the nested interface
        class HtmlFormatter : NotificationFormatter {
            override fun format(message: String): String {
                return "<html><body>$message</body></html>"
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {

        // Create the outer class instance
        val notifier = NotificationSystem()

        // Send notifications
        notifier.sendNotification("email", "user@example.com", "Your order has shipped!")
        notifier.sendNotification("sms", "+1234567890", "Your package has arrived.")

        // Use the static nested class
        val config = NotificationSystem.Configuration()
        config.defaultPriority = "HIGH"
        config.displayConfig()

        // Create instances of inner classes
        val emailNotifier = notifier.EmailNotification("support@example.com")
        emailNotifier.send("This is a test notification.")

        // Use the nested enum
        val highPriority = NotificationSystem.Priority.HIGH
        println("Priority: ${highPriority.getDisplayName()}")

        // Demonstrate name conflict resolution
        val event = notifier.NotificationEvent("Processing")
        event.logEvent()
    }
}
