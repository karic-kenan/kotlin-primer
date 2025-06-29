package collections

import java.time.LocalDate
import kotlin.random.Random

object CollectionsSequences {

    // Define data classes for our example
    data class Purchase(
        val id: String,
        val customerId: String,
        val amount: Double,
        val category: String,
        val date: String,
        val isCompleted: Boolean
    )

    data class Customer(
        val id: String,
        val name: String,
        val tier: String // "standard", "premium", or "elite"
    )

    data class Receipt(
        val purchaseId: String,
        val customerName: String,
        val amount: Double,
        val category: String,
        val date: String
    )

    // Generate sample data
    fun createSampleCustomers(): List<Customer> {
        return listOf(
            Customer("C001", "Alice Smith", "elite"),
            Customer("C002", "Bob Johnson", "standard"),
            Customer("C003", "Carol Williams", "premium"),
            Customer("C004", "Dave Brown", "standard"),
            Customer("C005", "Eve Davis", "elite")
        )
    }

    fun createSamplePurchases(): List<Purchase> {
        val customers = createSampleCustomers()
        val categories = listOf("Electronics", "Clothing", "Books", "Home", "Beauty")
        val random = Random(42) // Using a seed for reproducible results

        val purchases = mutableListOf<Purchase>()

        // Generate 1000 random purchases
        repeat(1000) { index ->
            val customerId = customers[random.nextInt(customers.size)].id
            val amount = 10 + random.nextDouble() * 490 // Amount between $10 and $500
            val category = categories[random.nextInt(categories.size)]
            val day = 1 + random.nextInt(28)
            val month = 1 + random.nextInt(12)
            val date = "2025-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
            val isCompleted = random.nextDouble() < 0.9 // 90% of purchases are completed

            purchases.add(
                Purchase(
                    id = "P${index.toString().padStart(4, '0')}",
                    customerId = customerId,
                    amount = amount,
                    category = category,
                    date = date,
                    isCompleted = isCompleted
                )
            )
        }

        return purchases
    }

    // Process data using regular collections
    fun processWithRegularCollections(
        purchases: List<Purchase>,
        customers: List<Customer>
    ): List<Receipt> {
        // Create a map for quick customer lookup
        val customerMap = customers.associateBy { it.id }

        // Start timing
        val startTime = System.nanoTime()

        // Step 1: Filter out incomplete purchases
        val completedPurchases = purchases.filter { it.isCompleted }

        // Step 2: Transform purchases to receipts with customer names
        val receipts = completedPurchases.map { purchase ->
            val customer = customerMap[purchase.customerId]
                ?: throw IllegalStateException("Customer not found for ID: ${purchase.customerId}")

            Receipt(
                purchaseId = purchase.id,
                customerName = customer.name,
                amount = purchase.amount,
                category = purchase.category,
                date = purchase.date
            )
        }

        // Step 3: Find top 5 receipts by amount
        val topReceipts = receipts.sortedByDescending { it.amount }.take(5)

        // End timing
        val endTime = System.nanoTime()
        val durationMs = (endTime - startTime) / 1_000_000.0

        println("Regular collection processing took $durationMs ms")
        return topReceipts
    }

    // Process data using sequences
    fun processWithSequences(
        purchases: List<Purchase>,
        customers: List<Customer>
    ): List<Receipt> {
        // Create a map for quick customer lookup
        val customerMap = customers.associateBy { it.id }

        // Start timing
        val startTime = System.nanoTime()

        // Use asSequence to convert the list to a sequence
        val topReceipts = purchases.asSequence()
            // Step 1: Filter out incomplete purchases
            .filter { it.isCompleted }
            // Step 2: Transform purchases to receipts with customer names
            .map { purchase ->
                val customer = customerMap[purchase.customerId]
                    ?: throw IllegalStateException("Customer not found for ID: ${purchase.customerId}")

                Receipt(
                    purchaseId = purchase.id,
                    customerName = customer.name,
                    amount = purchase.amount,
                    category = purchase.category,
                    date = purchase.date
                )
            }
            // Step 3: Find top 5 receipts by amount
            .sortedByDescending { it.amount }
            .take(5)
            // Terminal operation to convert back to a list
            .toList()

        // End timing
        val endTime = System.nanoTime()
        val durationMs = (endTime - startTime) / 1_000_000.0

        println("Sequence processing took $durationMs ms")
        return topReceipts
    }

    // Advanced sequence operations
    fun calculateAverageByCategoryWithSequences(purchases: List<Purchase>): Map<String, Double> {
        return purchases.asSequence()
            // Consider only completed purchases
            .filter { it.isCompleted }
            // Group by category
            .groupBy { it.category }
            // Transform each group into a pair of (category, average amount)
            .mapValues { (_, purchasesInCategory) ->
                purchasesInCategory.asSequence()
                    .map { it.amount }
                    .average()
            }
    }


    fun identifyPotentialFraudWithSequences(purchases: List<Purchase>): List<List<Purchase>> {
        // Sort purchases by customer ID and date
        val sortedPurchases = purchases.sortedWith(
            compareBy<Purchase> { it.customerId }.thenBy { it.date }
        )

        return sortedPurchases.asSequence()
            // Create sliding windows of 3 consecutive purchases
            .windowed(size = 3, step = 1)
            // Keep only windows where all purchases are from the same customer
            .filter { window ->
                window.all { it.customerId == window[0].customerId }
            }
            // Keep only windows where all purchases happened on the same date
            .filter { window ->
                window.all { it.date == window[0].date }
            }
            // Keep only windows where the total amount exceeds $1000
            .filter { window ->
                window.sumOf { it.amount } > 1000.0
            }
            .toList()
    }

    fun generateMonthlyReportWithSequences(
        purchases: List<Purchase>,
        customers: List<Customer>
    ): Map<String, Map<String, Double>> {
        val customerMap = customers.associateBy { it.id }

        return purchases.asSequence()
            // Consider only completed purchases
            .filter { it.isCompleted }
            // Extract month from date
            .map { purchase ->
                val month = purchase.date.substring(0, 7) // Format: YYYY-MM
                val customer = customerMap[purchase.customerId]
                    ?: throw IllegalStateException("Customer not found for ID: ${purchase.customerId}")

                Triple(month, customer.tier, purchase.amount)
            }
            // Group by month
            .groupBy { it.first }
            // For each month, calculate total spending by customer tier
            .mapValues { (_, triples) ->
                triples.asSequence()
                    .groupBy { it.second } // Group by customer tier
                    .mapValues { (_, triplesForTier) ->
                        triplesForTier.sumOf { it.third } // Sum amounts
                    }
            }
    }

    // Demonstrating infinite sequences
    fun generateDateSequence(startDate: LocalDate): Sequence<String> {
        return generateSequence(startDate) { it.plusDays(1) }
            .map { it.toString() }
    }

    fun forecastPurchasesWithSequences(
        purchases: List<Purchase>,
        forecastDays: Int
    ): List<Purchase> {
        val random = Random(42) // Using a seed for reproducible results

        // Calculate average daily purchases from historical data
        val purchasesByDate = purchases.groupBy { it.date }
        val averageDailyPurchases = purchasesByDate.size / purchasesByDate.keys.size.toDouble()

        // Get the latest date from historical data
        val latestDate = purchasesByDate.keys.maxOrNull()
            ?: throw IllegalStateException("No purchase dates found")

        // Create a starting point for our date sequence
        val startDate = LocalDate.parse(latestDate).plusDays(1)

        // Generate forecast using an infinite sequence of dates
        return generateDateSequence(startDate)
            // Take only the requested number of days
            .take(forecastDays)
            // For each date, generate forecasted purchases
            .flatMap { date ->
                // Determine how many purchases to forecast for this date
                val purchaseCount = (averageDailyPurchases * (0.8 + random.nextDouble() * 0.4)).toInt()

                // Generate that many random purchases
                sequence {
                    repeat(purchaseCount) { index ->
                        // Create a forecasted purchase based on patterns in historical data
                        val randomHistoricalPurchase = purchases[random.nextInt(purchases.size)]

                        yield(
                            Purchase(
                                id = "F${date.replace("-", "")}${index.toString().padStart(3, '0')}",
                                customerId = randomHistoricalPurchase.customerId,
                                amount = randomHistoricalPurchase.amount * (0.9 + random.nextDouble() * 0.2),
                                category = randomHistoricalPurchase.category,
                                date = date,
                                isCompleted = true
                            )
                        )
                    }
                }
            }
            .toList()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // Generate sample data
        val customers = createSampleCustomers()
        val purchases = createSamplePurchases()

        println("Generated ${purchases.size} purchases for ${customers.size} customers")

        // Compare regular collections vs sequences
        val topReceiptsRegular = processWithRegularCollections(purchases, customers)
        val topReceiptsSequence = processWithSequences(purchases, customers)

        // Verify results are the same
        println("\nTop 5 receipts (by amount):")
        topReceiptsSequence.forEach { receipt ->
            println("${receipt.purchaseId}: $${String.format("%.2f", receipt.amount)} by ${receipt.customerName}")
        }

        // Calculate average by category
        val averagesByCategory = calculateAverageByCategoryWithSequences(purchases)
        println("\nAverage purchase amount by category:")
        averagesByCategory.forEach { (category, average) ->
            println("$category: $${String.format("%.2f", average)}")
        }

        // Identify potential fraud
        val potentialFraud = identifyPotentialFraudWithSequences(purchases)
        println("\nPotential fraud detected (${potentialFraud.size} cases):")
        potentialFraud.take(3).forEach { window ->
            val totalAmount = window.sumOf { it.amount }
            println(
                "Customer ${window[0].customerId} made ${window.size} purchases totaling $${
                    String.format(
                        "%.2f",
                        totalAmount
                    )
                } on ${window[0].date}"
            )
        }

        // Generate monthly report
        val monthlyReport = generateMonthlyReportWithSequences(purchases, customers)
        println("\nMonthly spending by customer tier (first 3 months):")
        monthlyReport.entries.take(3).forEach { (month, tierData) ->
            println("$month:")
            tierData.forEach { (tier, amount) ->
                println("  $tier: $${String.format("%.2f", amount)}")
            }
        }

        // Forecast future purchases
        val forecastDays = 7
        val forecastedPurchases = forecastPurchasesWithSequences(purchases, forecastDays)
        println("\nForecasted ${forecastedPurchases.size} purchases for the next $forecastDays days")
        println("Sample forecasted purchases:")
        forecastedPurchases.take(5).forEach { purchase ->
            println(
                "${purchase.date}: $${
                    String.format(
                        "%.2f",
                        purchase.amount
                    )
                } in ${purchase.category} by Customer ${purchase.customerId}"
            )
        }
    }
}