package functions

import kotlin.collections.average
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.sortedByDescending
import kotlin.text.format

object LambdaFunctions {

    // Define a data class for employees with name, score, and department
    data class Employee(
        val name: String,
        val score: Int,
        val department: String,
        val yearsOfService: Int
    )

    // Higher-order function that accepts an operation returning a Boolean
    fun evaluateEmployee(employee: Employee, criteria: (Employee) -> Boolean): String {
        return if (criteria(employee)) {
            "${employee.name} meets the criteria"
        } else {
            "${employee.name} does not meet the criteria"
        }
    }

    fun findEligibleEmployee(employees: List<Employee>, eligibilityChecker: (Employee) -> Boolean): Employee? {
        for (employee in employees) {
            if (eligibilityChecker(employee)) {
                return employee
            }
        }
        return null
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // Create a list of employees with different scores and attributes
        val employees = listOf(
            Employee("Alice", 95, "Engineering", 5),
            Employee("Bob", 80, "Marketing", 3),
            Employee("Charlie", 65, "Engineering", 1),
            Employee("Diana", 90, "Finance", 7),
            Employee("Eve", 75, "Marketing", 2)
        )

        // Using a lambda - the 'return' statement only returns using labels
        println("\nUsing a lambda instead of anonymous function:")
        val directorPick = findEligibleEmployee(employees) { employee ->
            if (employee.score > 85) {
                return@findEligibleEmployee true // Using labeled return
            }
            if (employee.yearsOfService > 5) {
                return@findEligibleEmployee true // Using labeled return
            }
            false
        }
        println("Director's pick: ${directorPick?.name ?: "No eligible employee found"}")

        println("\nDEMONSTRATING TYPE SPECIFICATION BENEFITS:")

        // Create a function that calculates composite scores based on different algorithms
        fun scoreCalculator(algorithm: (Int, Int) -> Double): (Employee) -> Double {
            return { employee ->
                algorithm(employee.score, employee.yearsOfService)
            }
        }

        // Using a lambda with explicit parameter types
        val experienceWeightedScore = scoreCalculator { score: Int, years: Int ->
            // Explicit types make it clear which parameter is which
            score * (1 + years * 0.1)
        }

        // Print the calculated scores using both functions
        println("Experience-weighted scores:")
        employees.forEach {
            val score = experienceWeightedScore(it)
            println("${it.name}: $score")
        }

        println("\nCOMPLEX LOGIC WITH MULTIPLE RETURN POINTS:")

        val promotionChecker = { employee: Employee ->
            // For complex logic with multiple conditions, we use when for cleaner code
            when {
                employee.yearsOfService < 2 ->
                    "Ineligible: Insufficient tenure (minimum 2 years required)"

                employee.score < 70 ->
                    "Ineligible: Performance below threshold (minimum score 70 required)"

                employee.department == "Engineering" && employee.score < 80 ->
                    "Ineligible: Engineering requires higher performance (minimum score 80)"

                employee.score >= 90 ->
                    "Eligible: Outstanding performance"

                employee.yearsOfService >= 5 ->
                    "Eligible: Substantial experience"

                else ->
                    "Eligible: Meets standard criteria"
            }
        }

        // Evaluate each employee for promotion
        println("Promotion Eligibility:")
        employees.forEach { employee ->
            val result = promotionChecker(employee)
            println("${employee.name}: $result")
        }

        println("\nVARIABLE CAPTURE AND CONTEXT:")

        // Setting up a budget context
        var promotionBudget = 10000.0
        val averageScore = employees.map { it.score }.average()

        // Lambda that uses the context variables
        val promotionAmountCalculator = { employee: Employee ->
            // Using the captured variables from the outer scope
            if (promotionBudget <= 0) {
                0.0 // Budget exhausted
            } else {
                // Calculate promotion amount based on score compared to average
                val baseAmount = 1000.0
                val performanceMultiplier = employee.score / averageScore
                val amount = baseAmount * performanceMultiplier
                // Ensure we don't exceed the budget
                val finalAmount = kotlin.comparisons.minOf(amount, promotionBudget)
                promotionBudget -= finalAmount // Reduce the remaining budget

                finalAmount
            }
        }

        // Calculate and display promotion amounts
        println("Promotion Amounts:")
        employees.sortedByDescending { it.score }.forEach { employee ->
            val amount = promotionAmountCalculator(employee)
            println(
                "${employee.name}: ${String.format("%.2f", amount)} (Remaining budget: ${
                    String.format(
                        "%.2f",
                        promotionBudget
                    )
                })"
            )
        }

        println("\nCOMBINING LAMBDA FUNCTIONS:")

        // Define different evaluation criteria as lambdas
        val performanceCriteria = { emp: Employee -> emp.score >= 85 }
        val experienceCriteria = { emp: Employee -> emp.yearsOfService >= 4 }
        val departmentCriteria = { emp: Employee -> emp.department == "Engineering" }

        // Combine criteria using a lambda
        val combinedCriteria = { emp: Employee ->
            // We can use || and && operators for more concise logic
            (performanceCriteria(emp) && experienceCriteria(emp)) ||
                    (departmentCriteria(emp) && emp.score >= 75)
        }

        // Evaluate employees using combined criteria
        println("Employees Meeting Combined Criteria:")
        employees.forEach { employee ->
            val result = evaluateEmployee(employee, combinedCriteria)
            println(result)
        }
    }
}
