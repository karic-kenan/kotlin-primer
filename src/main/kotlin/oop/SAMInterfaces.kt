package oop

import kotlin.text.isNotBlank
import kotlin.text.uppercase

object SAMInterfaces {

    // First, let's define our event class
    data class Event(
        val type: String,
        val message: String,
        val timestamp: Long = System.currentTimeMillis()
    )

    // Creating our first SAM interface
    fun interface EventHandler {
        fun handle(event: Event)
    }

    // Creating an event manager to use our SAM interface
    class EventManager {
        private val handlers = mutableListOf<EventHandler>()

        // Method to register a new handler
        fun registerHandler(handler: EventHandler) {
            handlers.add(handler)
        }

        // Method to notify all handlers of an event
        fun notify(event: Event) {
            for (handler in handlers) {
                handler.handle(event)
            }
        }
    }

    // Using the traditional approach without SAM conversion
    fun demonstrateTraditional() {
        val eventManager = EventManager()

        // Traditional way with object expression
        val logHandler = object : EventHandler {
            override fun handle(event: Event) {
                println("[LOG] ${event.timestamp}: ${event.type} - ${event.message}")
            }
        }

        eventManager.registerHandler(logHandler)

        // Test with an event
        eventManager.notify(Event("INFO", "System started"))
    }

    // Using SAM conversion with lambda expressions
    fun demonstrateSAM() {
        val eventManager = EventManager()

        // Using SAM conversion with lambda
        eventManager.registerHandler { event ->
            println("[LOG] ${event.timestamp}: ${event.type} - ${event.message}")
        }

        // Adding another handler with SAM conversion
        eventManager.registerHandler { event ->
            if (event.type == "ERROR") {
                println("[ALERT] Critical error: ${event.message}")
            }
        }

        // Test with events
        eventManager.notify(Event("INFO", "User logged in"))
        eventManager.notify(Event("ERROR", "Database connection failed"))
    }

    // Storing SAM interface implementations in variables
    fun demonstrateVariables() {
        // Full syntax with explicit parameter
        val logHandler = EventHandler { event ->
            println("[LOG] ${event.timestamp}: ${event.type} - ${event.message}")
        }

        // Shorter syntax with implicit 'it' parameter
        val errorHandler = EventHandler {
            if (it.type == "ERROR") {
                println("[ALERT] Critical error: ${it.message}")
            }
        }

        val eventManager = EventManager()
        eventManager.registerHandler(logHandler)
        eventManager.registerHandler(errorHandler)

        // Test with events
        eventManager.notify(Event("INFO", "Application initialized"))
        eventManager.notify(Event("ERROR", "Memory limit exceeded"))
    }

    // First, let's create a similar system using function types
    class FunctionalEventManager {
        private val handlers = mutableListOf<(Event) -> Unit>()

        // Method to register a new handler using a function type
        fun registerHandler(handler: (Event) -> Unit) {
            handlers.add(handler)
        }

        // Method to notify all handlers of an event
        fun notify(event: Event) {
            for (handler in handlers) {
                handler(event)
            }
        }
    }

    fun demonstrateFunctionTypes() {
        val eventManager = FunctionalEventManager()

        // Registering handlers with function types
        eventManager.registerHandler { event ->
            println("[LOG] ${event.timestamp}: ${event.type} - ${event.message}")
        }

        // Test with an event
        eventManager.notify(Event("INFO", "Using function types"))
    }

    // Creating a more complex SAM interface with default methods
    fun interface DataProcessor {
        // The single abstract method
        fun process(data: String): String

        // Default method that builds on the abstract method
        fun processAndCount(data: String): Pair<String, Int> {
            val processed = process(data)
            return Pair(processed, processed.length)
        }

        // Another default method
        fun isValid(data: String): Boolean {
            return data.isNotBlank()
        }
    }

    fun demonstrateAdvancedSAM() {
        // Creating a processor with SAM conversion
        val uppercaseProcessor = DataProcessor { data ->
            data.uppercase()
        }

        // Using the abstract method
        val processed = uppercaseProcessor.process("Hello, SAM interfaces!")
        println("Processed: $processed")

        // Using a default method
        val (result, length) = uppercaseProcessor.processAndCount("Counting characters")
        println("Result: $result, Length: $length")

        // Using another default method
        println("Valid input: ${uppercaseProcessor.isValid("Test")}")
        println("Valid input: ${uppercaseProcessor.isValid("")}")
    }

    // A simple view class to simulate Android-like UI components
    open class View(open val id: String)

    // A button class that uses a SAM interface for click events
    class Button(override val id: String) : View(id) {
        // Define our SAM interface for click events
        fun interface OnClickListener {
            fun onClick(view: View)
        }

        private var clickListener: OnClickListener? = null

        // Method to set the click listener
        fun setOnClickListener(listener: OnClickListener) {
            this.clickListener = listener
        }

        // Method to simulate a button click
        fun performClick() {
            println("Button $id clicked")
            clickListener?.onClick(this)
        }
    }

    fun demonstrateButtonClick() {
        val button = Button("submit_button")

        // Setting a click listener with SAM conversion
        button.setOnClickListener { view ->
            println("Click handled for ${view.id}")
            // Perform some action in response to the click
        }

        // Simulating a button click
        button.performClick()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("=== Traditional Implementation ===")
        demonstrateTraditional()

        println("\n=== SAM Conversion with Lambdas ===")
        demonstrateSAM()

        println("\n=== SAM Interface Variables ===")
        demonstrateVariables()

        println("\n=== Function Types ===")
        demonstrateFunctionTypes()

        println("\n=== Advanced SAM Interface ===")
        demonstrateAdvancedSAM()

        println("\n=== Real-world Example: Button Click Listener ===")
        demonstrateButtonClick()
    }
}
