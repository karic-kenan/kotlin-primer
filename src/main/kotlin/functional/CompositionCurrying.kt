package functional

import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.sumOf
import kotlin.text.trimIndent
import kotlin.to

object CompositionCurrying {
    data class Transaction(val amount: Double, val type: String, val currency: String)

    // Simple function to demonstrate composition and currying
    private fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): Double {
        // Simulated conversion rates for simplicity
        val rates = mapOf(
            "EUR" to mapOf("USD" to 1.1, "GBP" to 0.85),
            "USD" to mapOf("EUR" to 0.91, "GBP" to 0.77),
            "GBP" to mapOf("EUR" to 1.18, "USD" to 1.3)
        )

        // Same currency
        if (fromCurrency == toCurrency) return amount

        // Get conversion rate or throw exception
        val rate = rates[fromCurrency]?.get(toCurrency)
            ?: throw kotlin.IllegalArgumentException("Unsupported currency conversion: $fromCurrency to $toCurrency")

        return amount * rate
    }

    // Filter by type
    private fun filterByType(type: String): (List<Transaction>) -> List<Transaction> {
        return { transactions -> transactions.filter { it.type == type } }
    }

    // Map and sum transaction amounts
    private fun sumAmounts(): (List<Transaction>) -> Double {
        return { transactions -> transactions.sumOf { it.amount } }
    }

    // CURRYING EXAMPLES

    // Curried version of convertCurrency - currying with multiple levels
    private fun convertCurrencyCurried(toCurrency: String): (String) -> (Double) -> Double {
        return { fromCurrency ->
            { amount ->
                convertCurrency(amount, fromCurrency, toCurrency)
            }
        }
    }

    // Alternative currying syntax - more typical in Kotlin
    private fun convertToUSD(): (Double, String) -> Double = { amount, fromCurrency ->
        convertCurrency(amount, fromCurrency, "USD")
    }

    // FUNCTION COMPOSITION EXAMPLES

    // Creating a higher-order function for function composition
    private fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C {
        return { x -> f(g(x)) }
    }

    // Example of processing pipeline using composition
    private fun createProcessingPipeline(type: String, targetCurrency: String): (List<Transaction>) -> Double {
        // Step 1: Filter transactions by type
        val filterFunc = filterByType(type)

        // Step 2: Convert all transactions to target currency
        val convertToCurrency = { transactions: List<Transaction> ->
            transactions.map {
                Transaction(
                    convertCurrency(it.amount, it.currency, targetCurrency),
                    it.type,
                    targetCurrency
                )
            }
        }

        // Step 3: Sum the amounts
        val sumFunc = { transactions: List<Transaction> ->
            transactions.sumOf { it.amount }
        }

        // Compose these functions together
        // This is manual composition, alternatively you could use the compose function
        return { transactions ->
            sumFunc(convertToCurrency(filterFunc(transactions)))
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val transactions = listOf(
            Transaction(100.0, "deposit", "EUR"),
            Transaction(50.0, "withdrawal", "USD"),
            Transaction(200.0, "deposit", "USD"),
            Transaction(75.0, "deposit", "GBP")
        )

        println("=== DEMONSTRATING CURRYING ===")

        // Using the multi-level curried function
        val toUSDConverter = convertCurrencyCurried("USD")
        val eurToUSD = toUSDConverter("EUR")
        val gbpToUSD = toUSDConverter("GBP")

        println("100 EUR to USD: ${eurToUSD(100.0)}")
        println("75 GBP to USD: ${gbpToUSD(75.0)}")

        // Alternative currying approach
        val directUSDConverter = convertToUSD()
        println("100 EUR to USD (alternative): ${directUSDConverter(100.0, "EUR")}")

        println("\n=== DEMONSTRATING COMPOSITION ===")

        // Using function composition
        val depositFilter = filterByType("deposit")
        val amountSummer = sumAmounts()

        // Manual composition
        val depositSum = { txs: List<Transaction> -> amountSummer(depositFilter(txs)) }
        println("Total deposits (manual composition): ${depositSum(transactions)}")

        // Using compose helper
        val getDepositSum = compose(amountSummer, depositFilter)
        println("Total deposits (using compose): ${getDepositSum(transactions)}")

        // Using the pipeline approach (combining filtering, conversion, and summation)
        val processDepositsToUSD = createProcessingPipeline("deposit", "USD")
        println("Total deposits in USD: ${processDepositsToUSD(transactions)}")

        println("\n=== REAL-WORLD APPLICATION ===")

        // Creating specialized processors with different configurations
        val getDepositTotalsInUSD = createProcessingPipeline("deposit", "USD")
        val getWithdrawalTotalsInEUR = createProcessingPipeline("withdrawal", "EUR")

        // Using these specialized functions - the power of composition and currying combined
        println("Total deposits in USD: ${getDepositTotalsInUSD(transactions)}")
        println("Total withdrawals in EUR: ${getWithdrawalTotalsInEUR(transactions)}")

        // Creating an analysis report function by composing other functions
        val analysisReport = { txs: List<Transaction> ->
            """
            Transaction Analysis:
            ---------------------
            Total deposits (USD): ${getDepositTotalsInUSD(txs)}
            Total withdrawals (EUR): ${getWithdrawalTotalsInEUR(txs)}
            Number of transactions: ${txs.size}
            """.trimIndent()
        }

        println("\n${analysisReport(transactions)}")
    }
}
