package basics

fun main() {
    val itemName = "Apples"
    val itemStock = 50
    val itemPrice = 1.99
    val customerBudget = 10.00

    // Imperative programming example
    if (itemStock > 0) {
        println("$itemName are available in stock.")
    } else {
        println("$itemName are out of stock.")
    }

    // Expression evaluated for different conditions
    val stockStatus: String = if (itemStock > 50) {
        "High stock of $itemName"
    } else if (itemStock in 20..50) {
        "Moderate stock of $itemName"
    } else {
        "Low stock of $itemName"
    }
    println(stockStatus)

    // Nested if statements
    val purchaseStatus: String = if (customerBudget >= itemPrice) {
        if (customerBudget >= itemPrice * 10) {
            "Customer can buy up to 10 $itemName"
        } else {
            "Customer can buy some $itemName"
        }
    } else {
        "Customer cannot afford $itemName"
    }
    println(purchaseStatus)

    // when -> improved switch
    val dayOfWeek = "Monday"

    when (dayOfWeek) {
        "Monday" -> println("10% off on $itemName")
        "Wednesday" -> println("Buy 1 get 1 free on $itemName")
        "Friday" -> println("20% off on all items")
        else -> println("No special offers today")
    }

    // Multiple conditions in a single when branch
    when (dayOfWeek) {
        "Saturday", "Sunday" -> println("Store is open from 10AM to 4PM")
        else -> println("Store is open from 9AM to 8PM")
    }

    // Returning a value from when
    val orderQuantity = 15

    val orderStatus = when (orderQuantity) {
        in 1..10 -> "Small order"
        in 11..50 -> "Medium order"
        else -> "Large order"
    }
    println(orderStatus)

    // Using when without an argument
    val customerAge = 25

    val ageGroup = when {
        customerAge < 18 -> "Minor"
        customerAge in 18..64 -> "Adult"
        customerAge >= 65 -> "Senior"
        else -> "Unknown age group"
    }
    println(ageGroup)

    // Using expressions in when branches
    val discountEligibility = when (customerAge) {
        16 + 9 -> "Eligible for adult discount"
        else -> "Not eligible for special discount"
    }
    println(discountEligibility)

    // Checking the type with when
    val customerFeedback: Any = "Great service!"

    val feedbackType = when (customerFeedback) {
        is Int -> "Feedback is a rating number"
        is String -> "Feedback is a comment"
        else -> "Unknown feedback type"
    }
    println(feedbackType)
}