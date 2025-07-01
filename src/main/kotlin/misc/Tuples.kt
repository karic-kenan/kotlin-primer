package misc

import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.maxByOrNull
import kotlin.collections.set
import kotlin.collections.sortedWith
import kotlin.collections.toMap
import kotlin.collections.withIndex
import kotlin.let
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.text.format
import kotlin.to

object Tuples {

    @JvmStatic
    fun main(args: Array<String>) {

        // Basic tuple creation and access
        println("\n=== Basic tuple creation and access ===")

        // Creating a pair using the constructor
        val coordinates = Pair(40.7128, -74.0060)  // New York coordinates

        // Accessing pair elements using properties
        println("Latitude: ${coordinates.first}")
        println("Longitude: ${coordinates.second}")

        // Creating a pair using the 'to' infix function
        val anotherCoordinates = 40.7128 to -74.0060
        println("Same coordinates: ${anotherCoordinates.first}, ${anotherCoordinates.second}")

        // Creating a triple
        val deliveryInfo = Triple("123 Broadway, NY", 2.5, "Express")

        // Accessing triple elements
        println("Address: ${deliveryInfo.first}")
        println("Weight: ${deliveryInfo.second}kg")
        println("Priority: ${deliveryInfo.third}")

        // Destructuring a pair
        val (lat, lng) = coordinates
        println("Unpacked coordinates: $lat, $lng")

        // Destructuring a triple
        val (address, weight, priority) = deliveryInfo
        println("Delivering a ${weight}kg package to $address with $priority priority")

        // Using tuples to return multiple values from functions
        println("\n=== Using tuples to return multiple values from functions ===")

        // Function that returns two values using a Pair
        fun calculateDistanceAndTime(
            origin: Pair<Double, Double>,
            destination: Pair<Double, Double>
        ): Pair<Double, Int> {
            // Simple calculation for demonstration purposes
            // In real-world, we would use proper geospatial formulas
            val lat1 = origin.first
            val lng1 = origin.second
            val lat2 = destination.first
            val lng2 = destination.second

            // Simplified distance calculation (not accurate for real use)
            val distance = sqrt((lat2 - lat1).pow(2.0) + (lng2 - lng1).pow(2.0)) * 111.0

            // Assuming an average speed of 50 km/h
            val timeInMinutes = ((distance / 50.0) * 60).toInt()

            return distance to timeInMinutes  // Using 'to' infix function to create the result pair
        }

        val newYork = 40.7128 to -74.0060
        val boston = 42.3601 to -71.0589

        val result = calculateDistanceAndTime(newYork, boston)
        println("Distance: ${result.first} km")
        println("Estimated time: ${result.second} minutes")

        // Using tuples in collections
        println("\n=== Using tuples in collections ===")

        // List of pairs
        val deliveryAreas = listOf(
            "Manhattan" to 5.99,
            "Brooklyn" to 4.99,
            "Queens" to 4.99,
            "Bronx" to 6.99,
            "Staten Island" to 7.99
        )

        // Iterating through the list of pairs
        println("Delivery Fees:")
        for ((area, fee) in deliveryAreas) {
            println("$area: $${fee}")
        }

        // Map with pairs as keys
        val routeInfo = mapOf(
            ("New York" to "Boston") to (346.0 to 240),
            ("New York" to "Philadelphia") to (151.0 to 120),
            ("Boston" to "Chicago") to (1373.0 to 960)
        )

        // Accessing and destructuring nested pairs
        println("\nRoute Information:")
        for ((route, distanceTime) in routeInfo) {
            val hours = distanceTime.second / 60
            val minutes = distanceTime.second % 60

            println("From ${route.first} to ${route.second}: ${distanceTime.first} km, $hours hours $minutes minutes")
        }

        // Finding the longest route
        val longestRoute = routeInfo.maxByOrNull { (_, distanceTime) -> distanceTime.first }
        if (longestRoute != null) {
            val (route, distanceTime) = longestRoute
            println("\nLongest route: From ${route.first} to ${route.second} at ${distanceTime.first} km")
        }

        // Tuple transformations and operations
        println("\n=== Tuple transformations and operations ===")

        val orders = listOf(
            Triple("ORD-1001", 2.5, 15.2),  // ID, weight (kg), distance (km)
            Triple("ORD-1002", 1.8, 7.6),
            Triple("ORD-1003", 3.7, 22.0),
            Triple("ORD-1004", 0.5, 3.2)
        )

        // Transforming a list of triples into a list of pairs
        val shippingCosts = orders.map { (id, weight, distance) ->
            // Calculate cost: $2 base fee + $0.5 per kg + $0.1 per km
            val cost = 2.0 + (weight * 0.5) + (distance * 0.1)
            id to cost  // Create a pair with the ID and calculated cost
        }

        // Displaying the shipping costs
        println("Shipping Costs:")
        shippingCosts.forEach { (id, cost) ->
            println("$id: $${String.format("%.2f", cost)}")
        }

        // Filtering based on multiple tuple values
        val heavyLongDistanceOrders = orders
            .filter { (_, weight, distance) -> weight > 2.0 && distance > 20.0 }
            .map { (id, _, _) -> id }

        println("\nHeavy, long-distance orders: $heavyLongDistanceOrders")

        // Sorting by multiple criteria using tuples
        val sortedByWeightAndDistance = orders.sortedWith(
            compareBy(
                { it.second },  // First sort by weight
                { it.third }    // Then by distance
            ))

        println("\nOrders sorted by weight and distance:")
        sortedByWeightAndDistance.forEach { (id, weight, distance) ->
            println("$id - $weight kg, $distance km")
        }

        // Comprehensive delivery tracking system example
        println("\n=== Comprehensive delivery tracking system example ===")

        // Data class for delivery destinations
        data class Destination(
            val name: String,
            val coordinates: Pair<Double, Double>,  // Latitude and Longitude
            val contactPerson: String? = null  // Optional contact person
        )

        // Calculate delivery cost based on weight and distance
        fun calculateDeliveryCost(weight: Double, distance: Double): Double {
            return 2.0 + (weight * 0.5) + (distance * 0.1)
        }

        // Calculate distance between two coordinate pairs
        fun calculateDistance(origin: Pair<Double, Double>, destination: Pair<Double, Double>): Double {
            // Simple calculation for demonstration purposes
            val latDiff = destination.first - origin.first
            val lngDiff = destination.second - origin.second
            return Math.sqrt(latDiff * latDiff + lngDiff * lngDiff) * 111.0  // Rough conversion to kilometers
        }

        // Estimate delivery time based on distance
        fun estimateDeliveryTime(distance: Double): Triple<Int, Int, Int> {
            // Returns hours, minutes, seconds
            val totalMinutes = (distance / 50.0) * 60  // Assuming 50 km/h average speed
            val hours = totalMinutes.toInt() / 60
            val minutes = totalMinutes.toInt() % 60
            val seconds = ((totalMinutes - totalMinutes.toInt()) * 60).toInt()
            return Triple(hours, minutes, seconds)
        }

        class DeliveryTracker {
            private val warehouse = 40.7128 to -74.0060  // Warehouse coordinates (New York)
            private val deliveries =
                mutableMapOf<String, Triple<Destination, Double, String>>()  // ID -> (Destination, Weight, Status)

            // Register a new delivery
            fun registerDelivery(
                id: String,
                destination: Destination,
                weight: Double
            ): Pair<Double, Triple<Int, Int, Int>> {
// Calculate distance
                val distance = calculateDistance(warehouse, destination.coordinates)

                // Calculate cost
                val cost = calculateDeliveryCost(weight, distance)

                // Estimate time
                val time = estimateDeliveryTime(distance)

                // Store the delivery information
                deliveries[id] = Triple(destination, weight, "Registered")

                // Return cost and estimated time
                return cost to time
            }

            // Update delivery status
            fun updateDeliveryStatus(id: String, newStatus: String): Boolean {
                val delivery = deliveries[id] ?: return false
                deliveries[id] = Triple(delivery.first, delivery.second, newStatus)
                return true
            }

            // Get delivery details
            fun getDeliveryDetails(id: String): Triple<Destination, Double, String>? {
                return deliveries[id]
            }

            // Get all deliveries
            fun getAllDeliveries(): Map<String, Triple<Destination, Double, String>> {
                return deliveries.toMap()  // Return a copy to prevent external modifications
            }
        }

        val tracker = DeliveryTracker()

        // Create some destinations
        val destinations = listOf(
            Destination("Boston", 42.3601 to -71.0589, "John Smith"),
            Destination("Philadelphia", 39.9526 to -75.1652, "Jane Doe"),
            Destination("Chicago", 41.8781 to -87.6298)
        )

        // Register deliveries
        println("Registering deliveries...")
        val results = mutableListOf<Triple<String, Double, Triple<Int, Int, Int>>>()

        for ((index, destination) in destinations.withIndex()) {
            val id = "DEL-${1001 + index}"
            val weight = 1.0 + (index * 1.5)  // Different weights

            val (cost, time) = tracker.registerDelivery(id, destination, weight)
            results.add(Triple(id, cost, time))
        }

        // Display registration results
        println("\nDelivery Registration Summary:")
        for ((id, cost, time) in results) {
            val (hours, minutes, seconds) = time
            println("$id: $${String.format("%.2f", cost)}, ETA: ${hours}h ${minutes}m ${seconds}s")
        }

        // Update some delivery statuses
        println("\nUpdating delivery statuses...")
        tracker.updateDeliveryStatus("DEL-1001", "In Transit")
        tracker.updateDeliveryStatus("DEL-1002", "Out for Delivery")

        // Get all deliveries and their current status
        println("\nCurrent Delivery Status:")
        for ((id, deliveryInfo) in tracker.getAllDeliveries()) {
            val (destination, weight, status) = deliveryInfo
            println("$id: Delivering ${weight}kg package to ${destination.name} - Status: $status")
            destination.contactPerson?.let { contact ->
                println("  Contact: $contact")
            }
        }
    }
}
