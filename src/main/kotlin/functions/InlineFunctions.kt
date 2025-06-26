package functions

object InlineFunctions {
    // Order data model
    data class Order(val id: String, val amount: Double)

    // Payment method data model
    data class PaymentMethod(val type: String, val details: String)

    // Transaction result data model
    data class TransactionResult(val success: Boolean, val message: String)

    // Regular (non-inline) function with lambda parameter for comparison
    private fun regularValidation(order: Order, validationLogic: (Order) -> Boolean): Boolean {
        println("Regular validation of order: ${order.id}")
        return validationLogic(order)
    }

    // Inline function to validate the order
    private inline fun validateOrder(order: Order, validationLogic: (Order) -> Boolean): Boolean {
        println("Validating order: ${order.id}")
        val isValid = validationLogic(order)
        if (!isValid) {
            println("Order validation failed for order: ${order.id}")
            return false
        }
        println("Order validation passed for order: ${order.id}")
        return true
    }

    // Inline function demonstrating non-local returns
    private inline fun withTransaction(action: () -> Boolean): Boolean {
        println("Starting transaction")
        try {
            val result = action()
            println("Transaction completed with result: $result")
            return result
        } catch (e: Exception) {
            println("Transaction failed: ${e.message}")
            return false
        } finally {
            println("Cleaning up transaction resources")
        }
    }

    // Inline function with crossinline parameter to prevent non-local returns
    private inline fun schedulePaymentProcessing(
        order: Order,
        crossinline paymentAction: (Order) -> Unit
    ) {
        println("Scheduling payment processing for order: ${order.id}")
        // Simulating scheduling with a Runnable (where non-local returns would be illegal)
        val runnable = Runnable {
            println("Executing scheduled payment for order: ${order.id}")
            paymentAction(order) // Here crossinline prevents non-local returns
        }
        // In real code this might be scheduled on a different thread or delayed
        runnable.run()
    }

    // Inline function with both crossinline and noinline parameters
    private inline fun processPayment(
        order: Order,
        paymentMethod: PaymentMethod,
        crossinline preProcessing: (Order) -> Unit,
        noinline transactionLogic: (Order, PaymentMethod) -> TransactionResult
    ): TransactionResult {
        println("Starting payment processing for order: ${order.id}")

        // Pre-processing logic benefits from inlining but can't use non-local returns
        schedulePaymentProcessing(order) {
            preProcessing(it)
            println("Pre-processing completed for order: ${it.id}")
        }

        // Complex transaction logic that shouldn't be inlined
        // Can be stored in variables or passed to other functions
        return transactionLogic(order, paymentMethod)
    }

    // Reified type parameter example - identify payment method type
    inline fun <reified T> identifyPaymentMethodType(paymentMethod: Any): Boolean {
        val result = paymentMethod is T
        println("Payment method is of type ${T::class.simpleName}: $result")
        return result
    }

    // Complex transaction logic that we don't want to inline
    fun complexTransaction(order: Order, paymentMethod: PaymentMethod): TransactionResult {
        println("Performing complex transaction logic for order: ${order.id}")
        println("Using payment method: ${paymentMethod.type} (${paymentMethod.details})")
        // Simulate complex logic, such as interacting with external payment gateways
        Thread.sleep(500) // Simulate delay
        return if (order.amount > 1000) {
            TransactionResult(false, "Transaction declined: amount exceeds limit")
        } else if (paymentMethod.type == "Credit Card") {
            TransactionResult(true, "Transaction approved via credit card")
        } else {
            TransactionResult(true, "Transaction approved via ${paymentMethod.type}")
        }
    }

    // Another complex function to store example of noinline
    fun storePaymentHandler(handler: (Order, PaymentMethod) -> TransactionResult) {
        println("Storing payment handler for later use")
        // In real code, you might store this in a repository or registry
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // Create an example order and payment method
        val order = Order(id = "12345", amount = 500.0)
        val paymentMethod = PaymentMethod(type = "Credit Card", details = "**** **** **** 1234")

        println("=== COMPARING REGULAR VS INLINE FUNCTION USAGE ===")
        // Show the difference between regular and inline function
        regularValidation(order) {
            println("Inside regular validation lambda")
            it.amount > 0
        }

        validateOrder(order) {
            println("Inside inline validation lambda")
            it.amount > 0
        }

        println("\n=== DEMONSTRATING NON-LOCAL RETURNS WITH INLINE ===")
        // Demonstrate non-local returns with inline functions
        val result = withTransaction {
            println("Inside transaction block")
            if (order.amount <= 0) {
                println("Invalid order amount, will return from withTransaction")
                return@withTransaction false // This returns from withTransaction function
            }

            // This is to demonstrate early return from the containing function
            if (order.amount > 10000) {
                println("Amount exceeds processing limit")
                return@main // This would return from the main function if amount > 10000
            }

            println("Order amount is valid")
            true
        }

        println("\n=== DEMONSTRATING CROSSINLINE AND NOINLINE ===")
        // Process the order using inline function with crossinline and noinline
        if (validateOrder(order) { it.amount > 0 }) {
            val transactionResult = processPayment(
                order,
                paymentMethod,
                preProcessing = {
                    println("Applying discount to order: ${it.id}")
                    // This lambda is inlined, but can't have non-local returns due to crossinline
                    // return@processPayment // This would cause a compile error if uncommented
                },
                transactionLogic = ::complexTransaction // No inlining here
            )

            // Store the transaction logic (only possible because of noinline)
            storePaymentHandler(::complexTransaction)

            if (transactionResult.success) {
                println("Payment processed successfully: ${transactionResult.message}")
            } else {
                println("Order processing failed: ${transactionResult.message}")
            }
        }

        println("\n=== DEMONSTRATING REIFIED TYPE PARAMETERS ===")
        // Demonstrate reified type parameters
        identifyPaymentMethodType<PaymentMethod>(paymentMethod)
        identifyPaymentMethodType<String>(paymentMethod)
        identifyPaymentMethodType<PaymentMethod>("Not a payment method")
    }
}
