package oop

import kotlin.collections.joinToString
import kotlin.collections.set
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty
import kotlin.text.contains
import kotlin.text.isEmpty
import kotlin.to

object Delegation {

    // Interface for task functionality
    interface Task {
        val title: String
        var isCompleted: Boolean
        fun complete()
        fun display()
    }

    // Basic implementation of the Task interface
    class BasicTask(override val title: String) : Task {

        override var isCompleted: Boolean = false

        override fun complete() {
            isCompleted = true
            println("Task '$title' has been completed")
        }

        override fun display() {
            val status = if (isCompleted) "COMPLETED" else "PENDING"
            println("Task: $title - Status: $status")
        }
    }

    // Interface for logging functionality
    interface Logger {
        fun log(message: String)
    }

    class ConsoleLogger : Logger {
        override fun log(message: String) {
            println("LOG: $message")
        }
    }

    // A file implementation of the Logger
    class FileLogger : Logger {
        override fun log(message: String) {
            // In a real application, this would write to a file
            println("FILE: Writing to log file: $message")
        }
    }

    // Task implementation with logging through delegation
    class LoggableTask(
        override val title: String,
        private val logger: Logger
    ) : Task, Logger by logger {
        override var isCompleted: Boolean = false

        override fun complete() {
            isCompleted = true
            // Using the log method delegated to logger
            log("Task '$title' has been completed")
        }

        override fun display() {
            val status = if (isCompleted) "COMPLETED" else "PENDING"
            log("Task: $title - Status: $status")
        }
    }

    // Task that overrides specific delegate methods
    class VerboseTask(
        override val title: String,
        logger: Logger
    ) : Task, Logger by logger {

        override var isCompleted: Boolean = false

        override fun complete() {
            isCompleted = true
            log("Verbose task '$title' has been completed")
        }

        override fun display() {
            val status = if (isCompleted) "COMPLETED" else "PENDING"
            log("Verbose task: $title - Status: $status")
        }

        override fun log(message: String) {
            // Overriding the delegated method with custom behavior
            println("VERBOSE LOG [${System.currentTimeMillis()}]: $message")
        }
    }

    // Task using standard property delegates
    class AdvancedTask(taskTitle: String) : Task {
        // Lazy property - initialized only when first accessed
        val description: String by lazy {
            println("Computing description for '$title'...")
            "This is a detailed description for task: $title"
        }

        // Observable property - notifies on changes
        var priority: Int by observable(0) { _, oldValue, newValue ->
            println("Task priority changed from $oldValue to $newValue")
        }

        // Custom delegate for the due date
        var dueDate: String by DateDelegate()

        override val title: String = taskTitle

        override var isCompleted: Boolean = false

        override fun complete() {
            isCompleted = true
            println("Advanced task '$title' completed with priority $priority")
        }

        override fun display() {
            println("ADVANCED TASK: $title [Priority: $priority]")
            println("Description: $description")
            println("Due date: $dueDate")
            println("Status: ${if (isCompleted) "COMPLETED" else "PENDING"}")
        }
    }

    // Custom property delegate for date handling
    class DateDelegate {
        private var date: String = "Not set"

        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return date
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            // Simple validation - in a real app, you'd do more sophisticated date parsing
            date = if (value.isEmpty()) {
                "Not set"
            } else {
                // For simplicity, just append "2025" if not present
                if (!value.contains("2025")) "$value, 2025" else value
            }
        }
    }

    // Task implementation using map-based properties
    class DynamicTask(map: MutableMap<String, Any?>) : Task {
        override val title: String by map
        override var isCompleted: Boolean by map

        var assignee: String by map
        var tags: List<String> by map

        override fun complete() {
            isCompleted = true
            println("Dynamic task '$title' completed by $assignee")
        }

        override fun display() {
            println("DYNAMIC TASK: $title")
            println("Assigned to: $assignee")
            println("Tags: ${tags.joinToString(", ")}")
            println("Status: ${if (isCompleted) "COMPLETED" else "PENDING"}")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {

        println("===== Basic Task =====")
        val basicTask = BasicTask("Write Kotlin lecture")
        basicTask.display()
        basicTask.complete()
        basicTask.display()
        println()

        println("===== Loggable Tasks =====")
        val consoleLogger = ConsoleLogger()
        val fileLogger = FileLogger()

        val consoleTask = LoggableTask("Prepare slides", consoleLogger)
        val fileTask = LoggableTask("Create examples", fileLogger)

        consoleTask.display()
        consoleTask.complete()

        fileTask.display()
        fileTask.complete()
        println()

        println("===== Verbose Task =====")
        val verboseTask = VerboseTask("Record video", consoleLogger)
        verboseTask.display()
        verboseTask.complete()
        println()

        println("===== Advanced Task =====")
        val advancedTask = AdvancedTask("Publish course materials")

        // First access will trigger lazy initialization
        println("Accessing description first time: ${advancedTask.description}")
        // Second access will use cached value
        println("Accessing description second time: ${advancedTask.description}")

        // Using observable property
        advancedTask.priority = 3
        advancedTask.priority = 5

        // Using custom delegate
        advancedTask.dueDate = "December 15"

        advancedTask.display()
        advancedTask.complete()
        println()

        println("===== Dynamic Task =====")
        val taskData = mutableMapOf<String, Any?>(
            "title" to "Review feedback",
            "isCompleted" to false,
            "assignee" to "John Doe",
            "tags" to listOf("feedback", "review", "education")
        )

        val dynamicTask = DynamicTask(taskData)
        dynamicTask.display()
        dynamicTask.complete()
        dynamicTask.display()

        // We can also update the map directly, which affects the task
        taskData["assignee"] = "Jane Smith"
        println("After changing assignee in the map:")
        dynamicTask.display()
    }
}
