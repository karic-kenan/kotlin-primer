package functional

fun calculateDiscountedPrice(price: Double, discount: Double): Double {
    return price - (price * discount / 100)
}

fun applyTax(price: Double, taxRate: Double): Double {
    return price + (price * taxRate / 100)
}

var globalDiscount = 10.0

fun calculateFinalPriceWithSideEffect(price: Double, taxRate: Double): Double {
    val discountedPrice = price - (price * globalDiscount / 100)
    return discountedPrice + (discountedPrice * taxRate / 100)
}

data class Product(val name: String, val price: Double)

fun applyDiscountToProduct(product: Product, discount: Double): Product {
    val discountedPrice = product.price - (product.price * discount / 100)
    return product.copy(price = discountedPrice)
}

fun main() {

    println("--- Demonstrating Pure Functions ---")
    val originalPrice = 100.0
    val discount = 20.0
    val taxRate = 15.0

    val discountedPrice = calculateDiscountedPrice(originalPrice, discount)
    val finalPrice = applyTax(discountedPrice, taxRate)
    println("Final Price (Pure Functions): $finalPrice") // Output: Final Price (Pure Functions): 92.0

    val secondDiscountedPrice = calculateDiscountedPrice(originalPrice, discount)
    val secondFinalPrice = applyTax(secondDiscountedPrice, taxRate)
    println("Final Price (Second Call): $secondFinalPrice") // Output: Final Price (Second Call): 92.0
    println("Are results identical? ${finalPrice == secondFinalPrice}") // Output: Are results identical? true

    println("\n--- Demonstrating Impure Functions and Side Effects ---")
    val firstResult = calculateFinalPriceWithSideEffect(originalPrice, taxRate)
    println("First call with globalDiscount = 10.0: $firstResult") // Output: First call with globalDiscount = 10.0: 103.5

    // Changing the global state
    globalDiscount = 5.0
    val secondResult = calculateFinalPriceWithSideEffect(originalPrice, taxRate)
    println("Second call with globalDiscount = 5.0: $secondResult") // Output: Second call with globalDiscount = 5.0: 109.25

    println("Same inputs but different results: ${firstResult != secondResult}") // Output: Same inputs but different results: true

    println("\n--- Demonstrating Immutability ---")
    val product = Product("Laptop", 1000.0)

    // Create a new object with the discount applied
    val discountedProduct = applyDiscountToProduct(product, discount)
    println("Original Product: $product")          // Output: Original Product: Product(name=Laptop, price=1000.0)
    println("Discounted Product: $discountedProduct") // Output: Discounted Product: Product(name=Laptop, price=800.0)

    // Simulate multiple operations using the same data
    println("\nSimulating concurrent access:")

    // Cart operation assumes original price
    val shippingCost = product.price * 0.05
    println("Shipping cost (5% of original price): $shippingCost") // Output: Shipping cost (5% of original price): 50.0

    // Discount operation uses discounted price
    val salesTax = discountedProduct.price * 0.08
    println("Sales tax (8% of discounted price): $salesTax") // Output: Sales tax (8% of discounted price): 64.0

    println("Both operations completed correctly because the original data was preserved.")
}
