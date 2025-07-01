package misc

import kotlin.collections.average
import kotlin.collections.map
import kotlin.collections.sumOf
import kotlin.text.format
import kotlin.text.trimIndent

object ValueClass {
    // Basic value classes for fitness metrics
    @JvmInline
    value class HeartRate(val value: Int) {
        fun display(): String {
            return "$value BPM"
        }

        val isElevated: Boolean
            get() = value > 100
    }

    @JvmInline
    value class StepCount(val value: Int) {
        fun estimateCaloriesBurned(): Double {
            // Simple estimation: 0.04 calories per step on average
            return value * 0.04
        }

        fun display(): String {
            return "$value steps"
        }
    }

    @JvmInline
    value class Calories(val value: Double) {
        fun toKilojoules(): Double {
            return value * 4.184
        }

        fun display(): String {
            return "%.1f calories".format(value)
        }
    }

    // Define an interface for displayable metrics
    interface Displayable {
        fun display(): String
    }

    // Create a value class implementing the interface
    @JvmInline
    value class Distance(val value: Double) : Displayable {
        override fun display(): String {
            return "%.2f meters".format(value)
        }

        fun toKilometers(): Double {
            return value / 1000
        }

        fun toMiles(): Double {
            return value / 1609.34
        }
    }

    // Creating a class that uses our value classes
    data class FitnessActivity(
        val duration: Int, // in minutes
        val averageHeartRate: HeartRate,
        val steps: StepCount,
        val distance: Distance,
        val caloriesBurned: Calories
    )

    // Creating a service that works with our value classes
    class FitnessTracker {
        private val activities = mutableListOf<FitnessActivity>()

        fun recordActivity(
            duration: Int,
            averageHeartRate: HeartRate,
            steps: StepCount,
            distance: Distance,
            caloriesBurned: Calories
        ) {
            val activity = FitnessActivity(
                duration,
                averageHeartRate,
                steps,
                distance,
                caloriesBurned
            )
            activities.add(activity)
            println("Activity recorded: ${activity.distance.display()} at ${activity.averageHeartRate.display()}")
        }

        fun totalDistance(): Distance {
            val sum = activities.sumOf { it.distance.value }
            return Distance(sum)
        }

        fun averageHeartRate(): HeartRate {
            if (activities.isEmpty()) return HeartRate(0)

            val average = activities.map { it.averageHeartRate.value }.average().toInt()
            return HeartRate(average)
        }

        fun totalCaloriesBurned(): Calories {
            val sum = activities.sumOf { it.caloriesBurned.value }
            return Calories(sum)
        }

        fun getSummary(): String {
            if (activities.isEmpty()) return "No activities recorded yet."

            return """
                
            Fitness Summary:
            Total activities: ${activities.size}
            Total distance: ${totalDistance().display()}
            Average heart rate: ${averageHeartRate().display()}
            Total calories burned: ${totalCaloriesBurned().display()}
            
        """.trimIndent()
        }
    }

    // Main function demonstrating the usage and benefits
    @JvmStatic
    fun main(args: Array<String>) {

        val tracker = FitnessTracker()

        // Record a walking activity
        tracker.recordActivity(
            duration = 30,
            averageHeartRate = HeartRate(85),
            steps = StepCount(3500),
            distance = Distance(2800.0),
            caloriesBurned = Calories(180.0)
        )

        // Record a running activity
        tracker.recordActivity(
            duration = 25,
            averageHeartRate = HeartRate(142),
            steps = StepCount(4200),
            distance = Distance(4200.0),
            caloriesBurned = Calories(320.0)
        )

        // This would cause a compile-time error if uncommented:
        // val wrongUsage: HeartRate = StepCount(1000) // Type mismatch

        // Print the fitness summary
        println(tracker.getSummary())

        // Demonstrate value class methods
        val myHeartRate = HeartRate(72)
        println("Is my heart rate elevated? ${if (myHeartRate.isElevated) "Yes" else "No"}")

        val myDistance = Distance(10000.0)
        println("Distance in kilometers: ${myDistance.toKilometers()} km")
        println("Distance in miles: ${myDistance.toMiles()} miles")

        val calories = Calories(450.0)
        println("Calories in kilojoules: ${calories.toKilojoules()} kJ")

        println("\nDemonstrating type-safe IDs:")
        demonstrateTypeIds()
    }

    // Value classes with generics for type-safe IDs
    interface Entity

    @JvmInline
    value class EntityId<T : Entity>(val value: Long)

    class User : Entity
    class Product : Entity

    fun fetchUser(id: EntityId<User>): User {
        // In a real application, this would fetch from a database
        println("Fetching user with ID: ${id.value}")
        return User()
    }

    fun fetchProduct(id: EntityId<Product>): Product {
        // In a real application, this would fetch from a database
        println("Fetching product with ID: ${id.value}")
        return Product()
    }

    fun demonstrateTypeIds() {
        val userId = EntityId<User>(1001L)
        val productId = EntityId<Product>(2002L)

        val user = fetchUser(userId)
        val product = fetchProduct(productId)

        // This would cause a compile-time error if uncommented:
        // fetchUser(productId) // Type mismatch
        // fetchProduct(userId) // Type mismatch

        println("Successfully fetched user and product with type-safe IDs")
    }
}
