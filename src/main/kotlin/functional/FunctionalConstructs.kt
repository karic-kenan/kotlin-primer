package functional

import kotlin.let
import kotlin.text.format
import kotlin.text.isEmpty

object FunctionalConstructs {

    // ====== DOMAIN MODELS ======
    // Immutable data classes for our domain

    /**
     * Represents a trip request from a user
     */
    data class TripRequest(
        val userId: String,
        val distanceInKm: Double,
        val baseFare: Double,
        val isPeakTime: Boolean,
        val promoCode: String?
    )

    /**
     * Represents a driver available for the trip
     */
    data class Driver(
        val driverId: String,
        val isAvailable: Boolean,
        val rating: Double
    )

    /**
     * Represents the final summary of the trip
     */
    data class TripSummary(
        val userId: String,
        val driverId: String,
        val totalFare: Double,
        val distanceInKm: Double
    )

    // ====== FUNCTIONAL CONSTRUCTS ======

    /**
     * Option pattern - A functional way to handle nullable values without null checks
     * This is similar to Scala's Option or Rust's Option
     */
    sealed class Option<out T> {
        data class Some<T>(val value: T) : Option<T>()
        object None : Option<Nothing>()

        companion object {
            // This utility function converts nullable types to Option
            fun <T> fromNullable(value: T?): Option<T> =
                if (value != null) Some(value) else None
        }
    }

    /**
     * Either pattern - A functional way to handle success/failure without exceptions
     * This is similar to Scala's Either or Rust's Result
     */
    sealed class Either<out L, out R> {
        data class Left<L>(val value: L) : Either<L, Nothing>()
        data class Right<R>(val value: R) : Either<Nothing, R>()
    }

    // ====== EXTENSION FUNCTIONS ======
    // Making our functional types more usable with extension functions

    /**
     * Map function for Option - Transform the value inside if it exists
     */
    private fun <T, R> Option<T>.map(transform: (T) -> R): Option<R> = when (this) {
        is Option.Some -> Option.Some(transform(value))
        is Option.None -> Option.None
    }

    /**
     * FlatMap function for Option - Transform and flatten nested Options
     */
    private fun <T, R> Option<T>.flatMap(transform: (T) -> Option<R>): Option<R> = when (this) {
        is Option.Some -> transform(value)
        is Option.None -> Option.None
    }

    /**
     * GetOrElse function for Option - Provide a default value if None
     */
    private fun <T> Option<T>.getOrElse(default: T): T = when (this) {
        is Option.Some -> value
        is Option.None -> default
    }

    /**
     * Map function for Either - Transform the Right value if it exists
     */
    private fun <L, R, R2> Either<L, R>.map(transform: (R) -> R2): Either<L, R2> = when (this) {
        is Either.Left -> this
        is Either.Right -> Either.Right(transform(value))
    }

    /**
     * Function composition helper
     */
    private infix fun <P1, R, R2> ((P1) -> R).andThen(next: (R) -> R2): (P1) -> R2 {
        return { p1: P1 -> next(this(p1)) }
    }

    // ====== PURE BUSINESS FUNCTIONS ======
    // These functions don't have side effects and always return the same output for the same input

    /**
     * Apply dynamic pricing based on peak times
     * Pure function: No side effects, same input always gives same output
     */
    private fun applySurgePricing(trip: TripRequest): TripRequest {
        val surgeMultiplier = if (trip.isPeakTime) 1.5 else 1.0
        val newBaseFare = trip.baseFare * surgeMultiplier
        // Returns a new instance, doesn't modify the original
        return trip.copy(baseFare = newBaseFare)
    }

    /**
     * Apply any promotions to the trip fare
     * Pure function: Takes inputs, returns new object
     */
    private fun applyPromotion(trip: TripRequest): TripRequest {
        val discount = when (trip.promoCode) {
            "SAVE10" -> 0.1 // 10% discount
            "FIRST50" -> 0.5 // 50% discount for first-time users
            else -> 0.0
        }
        val discountedFare = trip.baseFare * (1 - discount)
        return trip.copy(baseFare = discountedFare)
    }

    /**
     * Partial application example with promotion code
     * Returns a function that has "baked in" the promotion code
     */
    private fun applyPromotionWithCode(promoCode: String): (TripRequest) -> TripRequest {
        return { trip ->
            val discount = when (promoCode) {
                "SAVE10" -> 0.1
                "FIRST50" -> 0.5
                else -> 0.0
            }
            val discountedFare = trip.baseFare * (1 - discount)
            trip.copy(baseFare = discountedFare, promoCode = promoCode)
        }
    }

    /**
     * Check for driver availability using Option pattern
     * Returns Some(TripSummary) if driver is available, None otherwise
     */
    private fun checkDriverAvailability(trip: TripRequest, driver: Driver): Option<TripSummary> {
        return if (driver.isAvailable && driver.rating >= 4.5) {
            Option.Some(
                TripSummary(
                    userId = trip.userId,
                    driverId = driver.driverId,
                    totalFare = trip.baseFare,
                    distanceInKm = trip.distanceInKm
                )
            )
        } else {
            Option.None
        }
    }

    /**
     * Validate trip request using Either pattern
     * Returns Right(TripRequest) if valid, Left(String) with error message if invalid
     */
    private fun validateTripRequest(trip: TripRequest): Either<String, TripRequest> {
        return when {
            trip.distanceInKm <= 0 -> Either.Left("Distance must be positive")
            trip.baseFare <= 0 -> Either.Left("Base fare must be positive")
            trip.userId.isEmpty() -> Either.Left("User ID cannot be empty")
            else -> Either.Right(trip)
        }
    }

    /**
     * Format the trip summary for output
     * Pure function: Same input always gives same output
     */
    private fun formatTripSummary(tripSummary: TripSummary): String {
        return "User ID: ${tripSummary.userId}, Driver ID: ${tripSummary.driverId}, " +
                "Total Fare: $${"%.2f".format(tripSummary.totalFare)}, " +
                "Distance: ${tripSummary.distanceInKm} km"
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // Our sample data
        val tripRequest = TripRequest(
            userId = "user123",
            distanceInKm = 10.0,
            baseFare = 50.0,
            isPeakTime = true,
            promoCode = null
        )

        val driver = Driver(
            driverId = "driver456",
            isAvailable = true,
            rating = 4.9
        )

        // ====== DEMONSTRATION 0: USING fromNullable ======
        println("==== OPTION.fromNullable EXAMPLE ====")

        // Convert a nullable promo code to Option
        val promoOption = Option.fromNullable(tripRequest.promoCode)
        when (promoOption) {
            is Option.Some -> println("Promo code applied: ${promoOption.value}")
            is Option.None -> println("No promo code provided")
        }

        // ====== DEMONSTRATION 1: FUNCTION COMPOSITION ======"
        println("==== FUNCTION COMPOSITION EXAMPLE ====")

        // Validate first, then process if valid
        val processValidTrip: (TripRequest) -> Either<String, Option<TripSummary>> = { trip ->
            validateTripRequest(trip).map { validTrip ->
                // Process only happens if validation passed
                val processedTrip = applySurgePricing(validTrip)
                checkDriverAvailability(processedTrip, driver)
            }
        }

        // Execute and handle the result
        val result = processValidTrip(tripRequest)
        when (result) {
            is Either.Left -> println("Validation failed: ${result.value}")
            is Either.Right -> {
                when (val summary = result.value) {
                    is Option.Some -> println("Trip processed: ${formatTripSummary(summary.value)}")
                    is Option.None -> println("No driver available")
                }
            }
        }

        // ====== DEMONSTRATION 2: PARTIAL APPLICATION ======
        println("\n==== PARTIAL APPLICATION EXAMPLE ====")

        // Partially applied function with "SAVE10" promo code baked in
        val applySave10 = applyPromotionWithCode("SAVE10")

        // Using the partially applied function in a pipeline
        val discountedTrip = applySave10(tripRequest)
        println("Original fare: $${tripRequest.baseFare}")
        println("Discounted fare with SAVE10: $${discountedTrip.baseFare}")

        // Another partial application with different promo
        val applyFirstTimeUser = applyPromotionWithCode("FIRST50")
        val halfPriceTrip = applyFirstTimeUser(tripRequest)
        println("Half price fare with FIRST50: $${halfPriceTrip.baseFare}")

        // ====== DEMONSTRATION 3: FUNCTION PIPELINE WITH COMPOSITION ======
        println("\n==== FUNCTION PIPELINE EXAMPLE ====")

        // Method 1: Using custom andThen for function composition
        val tripPipeline = ::applySurgePricing andThen applySave10

        // Directly using applyPromotion in a different pipeline
        val anotherPipeline = ::applySurgePricing andThen ::applyPromotion
        val tripWithPromo = tripRequest.copy(promoCode = "SAVE10")
        val processedTripWithPromo = anotherPipeline(tripWithPromo)
        println("Using direct applyPromotion: Original fare: ${tripWithPromo.baseFare}, After promotion: ${processedTripWithPromo.baseFare}")

        // Reusable function to check with a specific driver
        val checkWithDriver: (TripRequest) -> Option<TripSummary> = { trip ->
            checkDriverAvailability(trip, driver)
        }

        // Complete pipeline with all steps
        val processTrip = tripPipeline andThen checkWithDriver andThen { option ->
            option.map(::formatTripSummary)
        }

        // Execute the pipeline
        val tripSummaryOption = processTrip(tripRequest)

        when (tripSummaryOption) {
            is Option.Some -> println("Pipeline result: ${tripSummaryOption.value}")
            is Option.None -> println("Pipeline result: No drivers available.")
        }

        // ====== DEMONSTRATION 4: ALTERNATIVE APPROACHES ======
        println("\n==== ALTERNATIVE APPROACHES ====")

        // Method 2: Using let for chaining
        val result2 = tripRequest
            .let(::applySurgePricing)
            .let(applySave10)
            .let { trip -> checkDriverAvailability(trip, driver) }
            .map(::formatTripSummary)
            .getOrElse("No drivers available.")

        println("Result using let chaining: $result2")

        // Method 3: Handling unavailable driver with Option
        val busyDriver = driver.copy(isAvailable = false)

        val noDriverResult = tripRequest
            .let(::applySurgePricing)
            .let(applySave10)
            .let { trip -> checkDriverAvailability(trip, busyDriver) }
            .map(::formatTripSummary)
            .getOrElse("No drivers available.")

        println("Result with busy driver: $noDriverResult")
    }
}
