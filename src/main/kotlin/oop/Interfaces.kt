package oop

// Interface for payment methods
interface PaymentMethod {
    val name: String
    val balance: Double

    fun processPayment(amount: Double): Boolean

    // Default method to display balance
    fun displayBalance() {
        println("The balance for $name is $$balance")
    }
}

// Implementing the interface for CreditCard payment
open class CreditCard(private val cardHolder: String, private var availableBalance: Double) : PaymentMethod {
    override val name: String
        get() = "Credit Card"

    override val balance: Double
        get() = availableBalance

    override fun processPayment(amount: Double): Boolean {
        return if (amount <= availableBalance) {
            availableBalance -= amount
            println("Payment of $$amount processed using $name.")
            true
        } else {
            println("Insufficient balance on $name.")
            false
        }
    }
}

// Implementing the interface for PayPal payment
class PayPal(private val accountEmail: String, private var availableBalance: Double) : PaymentMethod {
    override val name: String
        get() = "PayPal"

    override val balance: Double
        get() = availableBalance

    override fun processPayment(amount: Double): Boolean {
        return if (amount <= availableBalance) {
            availableBalance -= amount
            println("Payment of $$amount processed using $name.")
            true
        } else {
            println("Insufficient balance on $name.")
            false
        }
    }
}

// Another interface for rewards system
interface Rewards {
    val rewardPoints: Int

    fun addRewardPoints(points: Int)
}

// Implementing the interface for a Rewardable Credit Card
class RewardableCreditCard(
    cardHolder: String,
    availableBalance: Double,
    private var points: Int
) : CreditCard(cardHolder, availableBalance), Rewards {

    override val rewardPoints: Int
        get() = points

    override fun addRewardPoints(points: Int) {
        this.points += points
        println("Added $points points. Total reward points: ${this.rewardPoints}")
    }
}

fun main() {
    // Using CreditCard
    val creditCard = CreditCard("John Doe", 500.0)
    creditCard.displayBalance()
    creditCard.processPayment(150.0)
    creditCard.displayBalance()

    // Using PayPal
    val payPal = PayPal("john@example.com", 300.0)
    payPal.displayBalance()
    payPal.processPayment(100.0)
    payPal.displayBalance()

    // Using Rewardable CreditCard
    val rewardCard = RewardableCreditCard("Jane Doe", 700.0, 100)
    rewardCard.displayBalance()
    rewardCard.processPayment(200.0)
    rewardCard.displayBalance()
    rewardCard.addRewardPoints(50)
}
