package misc

import misc.Typealias.OrderItem
import misc.Typealias.Product

// Basic typealiases for our domain
typealias ProductId = String
typealias CustomerId = String
typealias OrderId = String
typealias Money = Double
typealias Quantity = Int

typealias Inventory = Map<ProductId, Product>

typealias OrderItems = List<OrderItem>

typealias CustomerOrderHistory = Map<OrderId, Typealias>

typealias CustomerDatabase = Map<CustomerId, CustomerOrderHistory>

// Typealiases for function types
typealias OrderProcessor = (Typealias.Order) -> Boolean

typealias PaymentCallback = (orderId: OrderId, success: Boolean, errorMessage: String?) -> Unit

typealias PriceCalculator = (basePrice: Money, quantity: Quantity) -> Money

typealias NotificationHandler<T> = (customerId: CustomerId, message: T) -> Unit

object Typealias {

    // Complex type structures made simpler with typealiases
    data class Product(val id: ProductId, val name: String, val description: String, val price: Money)

    data class OrderItem(val product: Product, val quantity: Quantity, val lineTotal: Money)

    data class Order(val id: OrderId, val customerId: CustomerId, val items: OrderItems, val total: Money)


    // Using our typealiases in service classes
    class OrderService(
        private val inventory: Inventory,
        private val customerDb: CustomerDatabase,
        private val priceCalculator: PriceCalculator,
        private val orderProcessor: OrderProcessor
    ) {
        fun createOrder(customerId: CustomerId, items: Map<ProductId, Quantity>): Order {
            val orderItems = mutableListOf<OrderItem>()

            var total: Money = 0.0

            for ((productId, quantity) in items) {
                // Find the product in inventory
                val product = inventory[productId] ?: throw IllegalArgumentException("Product not found: $productId")

                // Calculate the price for this line item
                val lineTotal = priceCalculator(product.price, quantity)

                // Add to our order items
                orderItems.add(OrderItem(product, quantity, lineTotal))

                // Add to total
                total += lineTotal
            }

            val orderId = "ORD-${System.currentTimeMillis()}" // Simplified ID generation
            return Order(orderId, customerId, orderItems, total)
        }

        fun processOrder(order: Order): Boolean {
            val success = orderProcessor(order)

            if (success) {
                // Get current customer history or create new one if this is first order
                val currentHistory = customerDb[order.customerId] ?: emptyMap()

                // Create updated history (immutable approach)
                val updatedHistory = currentHistory + (order.id to order)

                // In a real application, we would update the database here
                println("Updated order history for customer ${order.customerId}")
            }

            return success
        }
    }

    class PaymentService {
        fun processPayment(order: Order, callback: PaymentCallback) {
            // Simulate payment processing
            println("Processing payment for order ${order.id}, amount: ${order.total}")

            // Simulate success/failure (90% success rate)
            val success = Math.random() > 0.1

            if (success) {
                println("Payment successful for order ${order.id}")
                callback(order.id, true, null)
            } else {
                val errorMessage = "Payment gateway rejected the transaction"
                println("Payment failed for order ${order.id}: $errorMessage")
                callback(order.id, false, errorMessage)
            }
        }
    }

    // Different notification implementations using our generic function typealias
    class NotificationService {
        val emailNotifier: NotificationHandler<String> = { customerId, message ->
            println("Sending email to customer $customerId: $message")
        }

        val smsNotifier: NotificationHandler<String> = { customerId, message ->
            println("Sending SMS to customer $customerId: $message")
        }

        data class OrderStatus(val orderId: OrderId, val status: String, val estimatedDelivery: String)

        val orderUpdateNotifier: NotificationHandler<OrderStatus> = { customerId, status ->
            println("Notifying customer $customerId: Order ${status.orderId} is now ${status.status}")
            println("Estimated delivery: ${status.estimatedDelivery}")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {

        // Set up inventory
        val inventory: Inventory = mapOf(
            "PROD1" to Product("PROD1", "Laptop", "Powerful development machine", 1200.0),
            "PROD2" to Product("PROD2", "Mouse", "Ergonomic wireless mouse", 25.0),
            "PROD3" to Product("PROD3", "Keyboard", "Mechanical keyboard", 100.0)
        )

        // Initialize empty customer database
        val customerDb: CustomerDatabase = mutableMapOf()

        // Define a price calculator with quantity discount
        val priceCalculator: PriceCalculator = { basePrice, quantity ->
            when {
                quantity >= 10 -> basePrice * quantity * 0.9  // 10% discount for 10+ items
                quantity >= 5 -> basePrice * quantity * 0.95   // 5% discount for 5+ items
                else -> basePrice * quantity
            }
        }

        // Define an order processor
        val orderProcessor: OrderProcessor = { order ->
            println("Processing order ${order.id} for customer ${order.customerId}")
            println("Order contains ${order.items.size} items, total: ${order.total}")
            true // Simplified - always succeeds
        }

        // Create our services
        val orderService = OrderService(inventory, customerDb as CustomerDatabase, priceCalculator, orderProcessor)
        val paymentService = PaymentService()
        val notificationService = NotificationService()

        // Create a customer order
        val customerId: CustomerId = "CUST-001"

        val orderItems = mapOf(
            "PROD1" to 1, // One laptop
            "PROD2" to 2  // Two mice
        )

        // Create the order
        val order = orderService.createOrder(customerId, orderItems)
        println("Created order: ${order.id}, total: ${order.total}")

        // Process the order
        orderService.processOrder(order)

        // Define our payment callback inline using the typealias
        val paymentCallback: PaymentCallback = { orderId, success, errorMessage ->
            if (success) {
                println("Payment confirmed for order $orderId")

                // Send notification
                notificationService.emailNotifier(customerId, "Your order $orderId has been confirmed!")

                // Send order status update
                val orderStatus = NotificationService.OrderStatus(
                    orderId,
                    "Confirmed",
                    "Your order will arrive in 3-5 business days"
                )
                notificationService.orderUpdateNotifier(customerId, orderStatus)
            } else {
                println("Payment failed for order $orderId: $errorMessage")
                notificationService.emailNotifier(
                    customerId,
                    "There was a problem with your payment for order $orderId"
                )
            }
        }

        // Process payment
        paymentService.processPayment(order, paymentCallback)
    }
}
