package functions

// Unit -> "void" in other languages
fun showWelcomeMessage(): Unit {
    println("Welcome to the Restaurant Management System")
}

// Function that throws an exception, returning Nothing
fun validateUserInput(input: String?): Nothing {
    if (input.isNullOrEmpty()) {
        throw IllegalArgumentException("Input cannot be null or empty")
    }

    // This line will never be reached due to the throw statement
    error("This function should never return")
}

// Function accepting an argument
fun displayMenuItem(menuItem: String) {
    println("Menu Item: $menuItem")
}

// Function accepting an argument and returning a value
fun formatCustomerName(name: String): String {
    return "Customer Name: $name"
}

// Single-expression function
fun calculateBill(amount: Double, taxRate: Double): Double = amount + (amount * taxRate)

// Function overloading - Same function name with different parameters
fun calculateTotal(price: Double, quantity: Int): Double = price * quantity
fun calculateTotal(price: Double, quantity: Int, discount: Double): Double = (price * quantity) - discount

// Default arguments - Providing default values for parameters
fun prepareOrder(item: String, quantity: Int = 1): String = "Order: $item, Quantity: $quantity"
fun customerDetails(name: String, email: String = "No email provided", phone: String): String =
    "Customer Details -> Name: $name, Email: $email, Phone: $phone"

// Multi-line function - Performing more complex calculations
fun calculateTip(totalBill: Double, tipPercentage: Double): Double {
    val tipAmount = totalBill * (tipPercentage / 100)
    println("Performing some other complex operations.")
    return tipAmount
}

// Inner/local functions - Functions defined within another function
fun processOrder(orderType: String, price: Double, quantity: Int): Double {
    // Local function to apply discount
    fun applyDiscount(price: Double, discount: Double): Double {
        return price - (price * discount)
    }

    // Local function to add tax
    fun addTax(price: Double, taxRate: Double): Double {
        return price + (price * taxRate)
    }

    // Using local functions based on order type
    return when (orderType) {
        "discounted" -> applyDiscount(price, 0.1)
        "taxed" -> addTax(price, 0.05)
        else -> {
            println("Warning: Unrecognized order type '$orderType'. Proceeding with no modifications.")
            price
        }
    }
}

// Function with variable number of parameters (vararg) - Accepting multiple arguments
fun listSpecials(vararg items: String) {
    for (item in items) {
        println("Today's Special: $item")
    }
}

fun main() {

    // Calling simple function
    showWelcomeMessage()

    // Demonstrating function that throws an exception
    try {
        validateUserInput(null)
    } catch (e: IllegalArgumentException) {
        println("Caught IllegalArgumentException: ${e.message}")
    } catch (e: Exception) {
        println("Caught a general exception: ${e.message}")
    } finally {
        println("Error handling complete.")
    }

    // Calling function with an argument
    displayMenuItem("Grilled Chicken Salad")

    // Calling function with an argument and returning a value
    val customer = formatCustomerName("John Doe")
    println(customer)

    // Calling single-expression function
    val totalBill = calculateBill(50.0, 0.07)
    println("Total Bill with Tax: $$totalBill")

    // Calling overloaded functions
    println("Total for 3 items: $${calculateTotal(10.0, 3)}")
    println("Total for 3 items with $5 discount: $${calculateTotal(10.0, 3, 5.0)}")

    // Calling function with default arguments
    val singleOrder = prepareOrder("Burger")
    println(singleOrder)
    val order = prepareOrder("Pasta", 2)
    println(order)

    // Calling function with default and named arguments
    val details = customerDetails("Alice", phone = "123-456-7890")
    println(details)
    val detailedInfo = customerDetails(name = "Bob", email = "bob@example.com", phone = "987-654-3210")
    println(detailedInfo)

    // Calling multi-line function
    val tip = calculateTip(100.0, 15.0)
    println("Tip Amount: $$tip")

    // Calling function with inner/local functions
    val discountedOrder = processOrder("discounted", 50.0, 2)
    println("Discounted Order Price: $$discountedOrder")
    val taxedOrder = processOrder("taxed", 50.0, 2)
    println("Taxed Order Price: $$taxedOrder")

    // Calling function with variable number of parameters (vararg)
    listSpecials("Lobster Bisque", "Steak Tartare", "Truffle Pasta")
}