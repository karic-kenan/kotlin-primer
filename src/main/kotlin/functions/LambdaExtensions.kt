package functions

import functions.LambdaExtensions.Customer
import functions.LambdaExtensions.Order
import functions.LambdaExtensions.Product
import kotlin.collections.forEach

// Type aliases for our lambda receivers
typealias ProductOperation = Product.() -> Unit
typealias ProductTransformer<T> = Product.() -> T
typealias OrderProcessor = Customer.(Order) -> Boolean

object LambdaExtensions {

    // Warehouse domain classes
    data class Product(
        val id: String,
        val name: String,
        var quantity: Int,
        val price: Double,
        var isAvailable: Boolean = true
    )

    data class Customer(
        val id: String,
        val name: String,
        val email: String,
        val address: String,
        val orders: MutableList<Order> = mutableListOf()
    )

    data class Order(
        val id: String,
        val products: List<Product>,
        var status: OrderStatus = OrderStatus.PENDING
    )

    enum class OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

    fun Product.updateInventory(operation: ProductOperation): Product {
        operation()
        return this
    }

    fun <T> Product.transform(transformer: ProductTransformer<T>): T {
        return transformer()
    }

    fun Customer.processOrder(order: Order, processor: OrderProcessor): Boolean {
        return processor(order)
    }

    // Collection processing lambda extension
    fun List<Product>.processInventory(operation: ProductOperation): List<Product> {
        forEach { product ->
            product.operation()
        }
        return this
    }

    // Conditional lambda extension
    fun <T> T.whenCondition(
        condition: Boolean,
        operation: T.() -> Unit
    ): T {
        if (condition) {
            operation()
        }
        return this
    }

    // Custom implementation of scope functions
    fun <T> T.myApply(block: T.() -> Unit): T {
        block() // Execute the lambda with 'this' as receiver
        return this // Return the original object
    }

    fun <T, R> T.myLet(block: (T) -> R): R {
        return block(this) // Pass 'this' as an argument and return the result
    }

    fun <T, R> T.myRun(block: T.() -> R): R {
        return block() // Execute the lambda with 'this' as receiver and return the result
    }

    @JvmStatic
    fun main(args: Array<String>) {

        // Create sample products
        val laptop = Product("P001", "Gaming Laptop", 10, 1299.99)
        val phone = Product("P002", "Smartphone", 25, 699.99)
        val headphones = Product("P003", "Wireless Headphones", 30, 149.99)

        // Create a list of products
        val inventory = listOf(laptop, phone, headphones)

        // Create a customer
        val customer = Customer(
            "C001",
            "John Doe",
            "john@example.com",
            "123 Main St, Anytown, AN 12345"
        )

        // Create an order
        val order = Order(
            "O001",
            listOf(laptop.copy(quantity = 1), headphones.copy(quantity = 1))
        )

        // Add order to customer
        customer.orders.add(order)

        // Using updateInventory to update a product
        laptop.updateInventory {
            quantity -= 2
            isAvailable = quantity > 0
        }

        println("Updated laptop: $laptop")

        // Using transform to create a display string
        val laptopDisplay = laptop.transform {
            "$name - $$price (${if (isAvailable) "In Stock: $quantity" else "Out of Stock"})"
        }

        println("Product display: $laptopDisplay")

        // Using processOrder to process a customer order
        val orderProcessed = customer.processOrder(order) { currentOrder ->
            println("Processing order ${currentOrder.id} for customer $name")
            currentOrder.status = OrderStatus.PROCESSING
            true // Order successfully processed
        }

        println("Order processed: $orderProcessed, Status: ${order.status}")

        // Using processInventory on a collection
        inventory.processInventory {
            if (quantity < 20) {
                quantity += 5
                println("Restocked $name. New quantity: $quantity")
            }
        }

        // Using whenCondition to conditionally update a product
        phone.whenCondition(phone.quantity < 30) {
            quantity += 10
            println("Added more $name to inventory. New quantity: $quantity")
        }

        println("Final phone status: $phone")
    }
}
