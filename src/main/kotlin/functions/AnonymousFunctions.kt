package functions

import kotlin.collections.average
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.sortedByDescending
import kotlin.text.format

object AnonymousFunctions {

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

        // Using an anonymous function - the 'return' statement only returns from the anonymous function
        println("\nUsing an anonymous function with cleaner returns:")
        val directorPick = findEligibleEmployee(employees, fun(employee: Employee): Boolean {
            if (employee.score > 85) {
                return true // Directly returns from this function only
            }
            if (employee.yearsOfService > 5) {
                return true // Directly returns from this function only
            }
            return false
        })
        println("Director's pick: ${directorPick?.name ?: "No eligible employee found"}")

        println("\nDEMONSTRATING TYPE SPECIFICATION BENEFITS:")

        // Create a function that calculates composite scores based on different algorithms
        fun scoreCalculator(algorithm: (Int, Int) -> Double): (Employee) -> Double {
            return { employee ->
                algorithm(employee.score, employee.yearsOfService)
            }
        }

        // Using an anonymous function with explicit parameter types
        val experienceWeightedScore = scoreCalculator(
            fun(score: Int, years: Int): Double {
                // Explicit types make it clear which parameter is which
                return score * (1 + years * 0.1)
            }
        )

        // Print the calculated scores using both functions
        println("Experience-weighted scores:")
        employees.forEach {
            val score = experienceWeightedScore(it)
            println("${it.name}: $score")
        }

        println("\nCOMPLEX LOGIC WITH MULTIPLE RETURN POINTS:")

        val promotionChecker = fun(employee: Employee): String {
            // Multiple early returns based on different conditions
            if (employee.yearsOfService < 2) {
                return "Ineligible: Insufficient tenure (minimum 2 years required)"
            }

            if (employee.score < 70) {
                return "Ineligible: Performance below threshold (minimum score 70 required)"
            }

            if (employee.department == "Engineering" && employee.score < 80) {
                return "Ineligible: Engineering requires higher performance (minimum score 80)"
            }

            // Different qualification levels
            if (employee.score >= 90) {
                return "Eligible: Outstanding performance"
            }

            if (employee.yearsOfService >= 5) {
                return "Eligible: Substantial experience"
            }

            return "Eligible: Meets standard criteria"
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

        // Anonymous function that uses the context variables
        val promotionAmountCalculator = fun(employee: Employee): Double {
            // Using the captured variables from the outer scope
            if (promotionBudget <= 0) {
                return 0.0 // Budget exhausted
            }

            // Calculate promotion amount based on score compared to average
            val baseAmount = 1000.0
            val performanceMultiplier = employee.score / averageScore
            val amount = baseAmount * performanceMultiplier
            // Ensure we don't exceed the budget
            val finalAmount = kotlin.comparisons.minOf(amount, promotionBudget)
            promotionBudget -= finalAmount // Reduce the remaining budget

            return finalAmount
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

        println("\nCOMBINING ANONYMOUS FUNCTIONS:")

        // Define different evaluation criteria as anonymous functions
        val performanceCriteria = fun(emp: Employee): Boolean {
            return emp.score >= 85
        }

        val experienceCriteria = fun(emp: Employee): Boolean {
            return emp.yearsOfService >= 4
        }

        val departmentCriteria = fun(emp: Employee): Boolean {
            return emp.department == "Engineering"
        }

        // Combine criteria using an anonymous function
        val combinedCriteria = fun(emp: Employee): Boolean {
            // A function can return early if any primary criterion is met
            if (performanceCriteria(emp) && experienceCriteria(emp)) {
                return true
            }

            // Or if they're in the target department with decent performance
            if (departmentCriteria(emp) && emp.score >= 75) {
                return true
            }

            return false
        }

        // Evaluate employees using combined criteria
        println("Employees Meeting Combined Criteria:")
        employees.forEach { employee ->
            val result = evaluateEmployee(employee, combinedCriteria)
            println(result)
        }
    }
}
