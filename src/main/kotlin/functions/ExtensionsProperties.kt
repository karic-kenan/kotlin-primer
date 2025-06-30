package functions

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.collections.any
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.maxByOrNull
import kotlin.collections.sumOf
import kotlin.let
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object ExtensionsProperties {
    // Basic classes for our example
    data class User(
        val id: Int,
        val name: String,
        val membershipLevel: String,
        val signUpDate: String
    )

    data class Product(
        val id: Int,
        val name: String,
        val basePrice: Double,
        val category: String
    )

    // Basic extension properties for User
    val User.isPremium: Boolean
        get() = this.membershipLevel == "Premium"

    val User.isStandard: Boolean
        get() = this.membershipLevel == "Standard"

    val User.isBasic: Boolean
        get() = this.membershipLevel == "Basic"

    val User.membershipYears: Int
        get() {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val signUpDate = LocalDate.parse(this.signUpDate, formatter)
            val currentDate = LocalDate.now()
            return Period.between(signUpDate, currentDate).years
        }

    // Extension properties for discount calculations
    val Product.basicDiscount: Double
        get() = this.basePrice * 0.05

    val Product.standardDiscount: Double
        get() = this.basePrice * 0.1

    val Product.premiumDiscount: Double
        get() = this.basePrice * 0.15

    fun Product.discountedPriceFor(user: User): Double {
        return when {
            user.isPremium -> this.basePrice - this.premiumDiscount
            user.isStandard -> this.basePrice - this.standardDiscount
            user.isBasic -> this.basePrice - this.basicDiscount
            else -> this.basePrice
        }
    }

    // Extension properties with validation and complex logic
    val Product.isEligibleForPromotion: Boolean
        get() {
            // Products below $500 or not in the Laptop category aren't eligible
            if (this.basePrice < 500 || this.category != "Laptop") {
                return false
            }

            // Laptops priced at $500 or more are eligible for promotions
            return true
        }

    fun Product.loyaltyDiscountFor(user: User): Double {
        // Calculate additional discount: 1% per year of membership, up to 5%
        val loyaltyPercentage = kotlin.comparisons.minOf(user.membershipYears * 0.01, 0.05)
        return this.basePrice * loyaltyPercentage
    }

    fun Product.finalPriceFor(user: User): Double {
        val membershipDiscount = when {
            user.isPremium -> this.premiumDiscount
            user.isStandard -> this.standardDiscount
            user.isBasic -> this.basicDiscount
            else -> 0.0
        }

        val loyaltyDiscount = this.loyaltyDiscountFor(user)

        // Calculate final price by subtracting all discounts
        val finalPrice = this.basePrice - membershipDiscount - loyaltyDiscount

        // Ensure price doesn't go below 80% of base price (maximum 20% discount)
        val minimumPrice = this.basePrice * 0.8

        return kotlin.comparisons.maxOf(finalPrice, minimumPrice)
    }

    // Shopping cart class and mutable extension properties
    class ShoppingCart {
        val items = mutableListOf<Product>()
        val customer: User? = null
    }

    var ShoppingCart.totalPrice: Double
        get() = this.items.sumOf { it.basePrice }
        set(value) {
            // This is a bit artificial, but it demonstrates a setter
            // We'll adjust the cart contents to match the new total value
            if (items.isEmpty()) return

            val currentTotal = this.totalPrice
            if (currentTotal == 0.0) return

            val ratio = value / currentTotal
            // Create a new list with adjusted prices
            val newItems = items.map {
                Product(it.id, it.name, it.basePrice * ratio, it.category)
            }

            // Clear and repopulate items
            items.clear()
            items.addAll(newItems)
        }

    val ShoppingCart.finalPrice: Double
        get() {
            if (this.customer == null) return this.totalPrice

            return this.items.sumOf { it.finalPriceFor(this.customer) }
        }

    // Extension properties with delegation
    class FinalPriceDelegate : ReadOnlyProperty<ShoppingCart, Double> {
        private var _value: Double? = null
        private var lastItemCount = -1

        override fun getValue(thisRef: ShoppingCart, property: KProperty<*>): Double {
            // Recalculate only if the number of items has changed
            if (_value == null || lastItemCount != thisRef.items.size) {
                lastItemCount = thisRef.items.size
                _value = thisRef.items.sumOf { product ->
                    thisRef.customer?.let { product.finalPriceFor(it) } ?: product.basePrice
                }
            }
            return _value!!
        }
    }

    val ShoppingCart.cachedFinalPrice: Double by FinalPriceDelegate()

    // Generic extension properties for collections
    val Collection<Product>.mostExpensive: Product?
        get() = this.maxByOrNull { it.basePrice }

    val Collection<Product>.averagePrice: Double
        get() = if (isEmpty()) 0.0 else sumOf { it.basePrice } / size

    val Collection<Product>.laptops: List<Product>
        get() = this.filter { it.category == "Laptop" }

    val Collection<Product>.hasPremiumProducts: Boolean
        get() = this.any { it.basePrice > 1000 }

    @JvmStatic
    fun main(args: Array<String>) {

        // Creating users
        val basicUser = User(1, "Alex", "Basic", "2024-01-15")
        val standardUser = User(2, "Beth", "Standard", "2020-05-20")
        val premiumUser = User(3, "Charlie", "Premium", "2018-03-10")

        // Creating products
        val budgetLaptop = Product(101, "Budget Laptop", 650.0, "Laptop")
        val midRangeLaptop = Product(102, "Mid-Range Laptop", 1200.0, "Laptop")
        val premiumLaptop = Product(103, "Premium Laptop", 2500.0, "Laptop")

        // Using user extension properties
        println("User Membership Info:")
        println("${basicUser.name} is a basic member: ${basicUser.isBasic}")
        println("${standardUser.name} is a standard member: ${standardUser.isStandard}")
        println("${premiumUser.name} is a premium member: ${premiumUser.isPremium}")
        println("${standardUser.name} has been a member for ${standardUser.membershipYears} years")
        println("${premiumUser.name} has been a member for ${premiumUser.membershipYears} years")
        println()

        // Using discount extension properties
        println("Discount Information for ${midRangeLaptop.name} (${midRangeLaptop.basePrice}):")
        println("Basic discount: ${midRangeLaptop.basicDiscount}")
        println("Standard discount: ${midRangeLaptop.standardDiscount}")
        println("Premium discount: ${midRangeLaptop.premiumDiscount}")
        println()

        // Using validation properties
        val keyboard = Product(201, "Wireless Keyboard", 120.0, "Accessory")
        println("Promotion Eligibility:")
        println("${budgetLaptop.name} eligible for promotion: ${budgetLaptop.isEligibleForPromotion}")
        println("${premiumLaptop.name} eligible for promotion: ${premiumLaptop.isEligibleForPromotion}")
        println("${keyboard.name} eligible for promotion: ${keyboard.isEligibleForPromotion}")
        println()

        // Using loyalty discount calculations
        println("Loyalty Discounts:")
        println(
            "${basicUser.name} (${basicUser.membershipYears} years) loyalty discount on ${midRangeLaptop.name}: ${
                midRangeLaptop.loyaltyDiscountFor(
                    basicUser
                )
            }"
        )
        println(
            "${standardUser.name} (${standardUser.membershipYears} years) loyalty discount on ${midRangeLaptop.name}: ${
                midRangeLaptop.loyaltyDiscountFor(
                    standardUser
                )
            }"
        )
        println(
            "${premiumUser.name} (${premiumUser.membershipYears} years) loyalty discount on ${midRangeLaptop.name}: ${
                midRangeLaptop.loyaltyDiscountFor(
                    premiumUser
                )
            }"
        )
        println()

        // Creating a shopping cart
        val cart = ShoppingCart()
        cart.items.add(budgetLaptop)
        cart.items.add(keyboard)

        // Using shopping cart extension properties
        println("Shopping Cart:")
        println("Total price: ${cart.totalPrice}")

        // Using our setter to adjust the total price
        cart.totalPrice = cart.totalPrice * 0.9  // Apply a 10% discount
        println("After 10% discount: ${cart.totalPrice}")

        println("Cached final price: ${cart.cachedFinalPrice}")
        println()

        // Creating a product list
        val allProducts = listOf(budgetLaptop, midRangeLaptop, premiumLaptop, keyboard)

        // Using generic collection extension properties
        println("Product Collection Information:")
        println("Most expensive product: ${allProducts.mostExpensive?.name} at ${allProducts.mostExpensive?.basePrice}")
        println("Average price: ${allProducts.averagePrice}")
        println("Laptop products: ${allProducts.laptops.map { it.name }}")
        println("Collection has premium products: ${allProducts.hasPremiumProducts}")
    }
}
