package oop

import kotlin.collections.forEach
import kotlin.collections.joinToString
import kotlin.text.toUpperCase

/**
 * 11. Enums
 */
// Enum class for Traffic Light States
object Enums {

    // Basic enum definition
//    enum class ActivityType {
//        WALKING,
//        RUNNING,
//        SWIMMING,
//        CYCLING
//    }

    // Enum with properties
//    enum class ActivityType(val caloriesPerHour: Int) {
//        WALKING(300),
//        RUNNING(600),
//        SWIMMING(450),
//        CYCLING(500)
//    }

    // Enum with multiple properties
//    enum class ActivityType(
//        val caloriesPerHour: Int,
//        val isCardio: Boolean
//    ) {
//        WALKING(300, true),
//        RUNNING(600, true),
//        SWIMMING(450, true),
//        CYCLING(500, true)
//    }

    // Enum with methods
//    enum class ActivityType(val caloriesPerHour: Int) {
//        WALKING(300),
//        RUNNING(600),
//        SWIMMING(450),
//        CYCLING(500);  // Note the semicolon here
//
//        fun calculateCaloriesBurned(minutes: Int): Double {
//            return (caloriesPerHour / 60.0) * minutes
//        }
//    }

    // Interface for activity descriptions
//    interface Describable {
//        fun getDescription(): String
//    }

    // Enum implementing an interface
//    enum class ActivityType(val caloriesPerHour: Int) : Describable {
//        WALKING(300) {
//            override fun getDescription(): String = "Walking at a moderate pace"
//        },
//        RUNNING(600) {
//            override fun getDescription(): String = "Running at a steady pace"
//        },
//        SWIMMING(450) {
//            override fun getDescription(): String = "Swimming in a pool"
//        },
//        CYCLING(500) {
//            override fun getDescription(): String = "Cycling on flat terrain"
//        };
//
//        // Default implementation
//        override fun getDescription(): String = "Performing ${name.toLowerCase()}"
//    }

    // Enum with abstract methods
    enum class ActivityType(val caloriesPerHour: Int) {
        WALKING(300) {
            override fun equipmentNeeded(): List<String> = listOf("Comfortable shoes")
        },
        RUNNING(600) {
            override fun equipmentNeeded(): List<String> = listOf("Running shoes", "Breathable clothes")
        },
        SWIMMING(450) {
            override fun equipmentNeeded(): List<String> = listOf("Swimsuit", "Goggles", "Swim cap")
        },
        CYCLING(500) {
            override fun equipmentNeeded(): List<String> = listOf("Bicycle", "Helmet", "Water bottle")
        };

        // Abstract method declaration
        abstract fun equipmentNeeded(): List<String>
    }

    // Helper functions demonstrating enum usage
    fun printActivityName(activity: ActivityType) {
        println("Activity name: ${activity.name}")
    }

    fun printActivityOrder(activity: ActivityType) {
        println("${activity.name} is activity #${activity.ordinal + 1}")
    }

    fun getActivityByName(name: String): ActivityType? {
        return try {
            ActivityType.valueOf(name.uppercase())
        } catch (e: IllegalArgumentException) {
            null
        }
    }

//    fun listAllActivities() {
//        println("Available activities:")
//        ActivityType.entries.forEach { activity ->
//            println("- ${activity.name}: ${activity.getDescription()}")
//        }
//    }

    // Using enums with when expressions
    fun getActivityIcon(activity: ActivityType): String {
        return when (activity) {
            ActivityType.WALKING -> "🚶"
            ActivityType.RUNNING -> "🏃"
            ActivityType.SWIMMING -> "🏊"
            ActivityType.CYCLING -> "🚴"
        }
    }

    fun getDifficultyLevel(activity: ActivityType): Int {
        return when (activity) {
            ActivityType.WALKING -> 1
            ActivityType.CYCLING -> 2
            ActivityType.SWIMMING -> 3
            ActivityType.RUNNING -> 4
        }
    }

    // Complete example implementation
    @JvmStatic
    fun main(args: Array<String>) {

        // Accessing enum constants
        val myActivity = ActivityType.RUNNING
        println("My activity: ${myActivity.name}")

        // Using enum methods
//        val duration = 30 // minutes
//        val caloriesBurned = myActivity.calculateCaloriesBurned(duration)
//        println("Calories burned in $duration minutes of ${myActivity.name}: $caloriesBurned")

//        // Using interface methods
//        println("Description: ${myActivity.getDescription()}")

        // Using abstract methods
        println("Equipment needed: ${myActivity.equipmentNeeded().joinToString(", ")}")

        // Using built-in values() method
        println("\nAll activities:")
        ActivityType.entries.forEach { activity ->
            println("${activity.name} (${activity.ordinal}): burns ${activity.caloriesPerHour} calories per hour")
        }

        // Using valueOf() method
        try {
            val parsedActivity = ActivityType.valueOf("SWIMMING")
            println("\nParsed activity: ${parsedActivity.name}")
        } catch (e: IllegalArgumentException) {
            println("Invalid activity name")
        }

        // Using when expression with enums
        val newActivity = ActivityType.CYCLING
        val icon = getActivityIcon(newActivity)
        val difficulty = getDifficultyLevel(newActivity)
        println("\n${newActivity.name} icon: $icon")
        println("${newActivity.name} difficulty level: $difficulty")
    }
}
