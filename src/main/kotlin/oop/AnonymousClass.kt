package oop

import java.text.DecimalFormat
import java.time.LocalDateTime
import kotlin.text.format
import kotlin.text.lowercase

// Define an interface for payment processing
interface PaymentProcessor {
    fun processPayment(amount: Double): String
}

// Function to execute a payment with a specified processor
fun executePayment(amount: Double, processor: PaymentProcessor): String {
    return processor.processPayment(amount)
}

// Helper function to format the payment amount
fun formatAmount(amount: Double): String {
    val formatter = DecimalFormat("#,##0.00")
    return formatter.format(amount)
}

// Abstract class for online payment methods
abstract class OnlinePaymentMethod {
    abstract fun processPayment(amount: Double): String

    // Method that subclasses can use
    protected fun logTransaction(method: String, amount: Double) {
        println("[$method] Transaction logged at ${LocalDateTime.now()}")
    }
}

fun main() {
    // Variables in the outer scope that will be accessed by anonymous classes
    val merchantName = "E-Commerce Store"
    val transactionId = "TXN-${System.currentTimeMillis()}"
    val securityLevel = 3 // Out of 5

    // Creating anonymous classes for different payment methods
    val creditCardProcessor = object : PaymentProcessor {
        // Private properties specific to this anonymous class
        private val creditCardFee = 0.025 // 2.5%
        private val securityCheck = securityLevel > 2 // Using outer scope variable

        // Private method inside the anonymous class
        private fun calculateFee(amount: Double): Double {
            return amount * creditCardFee
        }

        override fun processPayment(amount: Double): String {
            val formattedAmount = formatAmount(amount)
            val fee = calculateFee(amount)
            val formattedFee = formatAmount(fee)

            return if (securityCheck) {
                "Processing secure credit card payment of $$formattedAmount for $merchantName " +
                        "(Transaction ID: $transactionId, Fee: $$formattedFee)"
            } else {
                "SECURITY ALERT: Transaction security level insufficient for credit card processing"
            }
        }
    }

    // Anonymous class that extends an abstract class rather than implementing an interface
    val paypalProcessor = object : OnlinePaymentMethod() {
        private val paypalEmail = "payments@$merchantName.com".lowercase()

        override fun processPayment(amount: Double): String {
            val formattedAmount = formatAmount(amount)
            // Using the parent class method
            logTransaction("PayPal", amount)
            return "Processing PayPal payment of $$formattedAmount through $paypalEmail " +
                    "(Transaction ID: $transactionId)"
        }
    }

    // Variable to track Bitcoin price for the anonymous class to access
    val bitcoinToDollarRate = 65000.00

    // Creating and using anonymous class directly at the call site
    val bitcoinProcessor = executePayment(0.015, object : PaymentProcessor {
        override fun processPayment(amount: Double): String {
            // Calculate Bitcoin value based on enclosing scope variable
            val dollarAmount = amount * bitcoinToDollarRate
            val formattedDollarAmount = formatAmount(dollarAmount)
            val formattedBtcAmount = "%.6f".format(amount)

            return "Processing Bitcoin payment of BTC $formattedBtcAmount " +
                    "($$formattedDollarAmount) with secure wallet for $merchantName"
        }
    })

    // Simulate different payment scenarios
    println(creditCardProcessor.processPayment(123.45))
    println(paypalProcessor.processPayment(67.89))
    println(bitcoinProcessor)
}
