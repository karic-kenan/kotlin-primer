package oop

// Abstract class Employee
abstract class Employee(val name: String, val age: Int) {

    // Abstract property to be implemented by derived classes
    abstract val position: String

    // Abstract method to calculate salary (must be implemented by derived classes)
    abstract fun calculateSalary(): Double

    // Concrete method to describe the employee
    fun describe() {
        println("This is $name, who is $age years old and works as a $position.")
    }
}

// Derived class Manager inheriting from Employee
class Manager(name: String, age: Int, val baseSalary: Double) : Employee(name, age) {

    // Implementing the abstract property
    override val position: String
        get() = "Manager"

    // Implementing the abstract method to calculate salary
    override fun calculateSalary(): Double {
        return baseSalary + (baseSalary * 0.1) // Base salary plus 10% bonus
    }
}

// Derived class Developer inheriting from Employee
class Developer(name: String, age: Int, val baseSalary: Double, val projectsCompleted: Int) : Employee(name, age) {

    // Implementing the abstract property
    override val position: String
        get() = "Developer"

    // Implementing the abstract method to calculate salary
    override fun calculateSalary(): Double {
        return baseSalary + (projectsCompleted * 500) // Base salary plus bonus per project
    }
}

// Derived class Intern inheriting from Employee
class Intern(name: String, age: Int, val hourlyRate: Double, val hoursWorked: Int) : Employee(name, age) {

    // Implementing the abstract property
    override val position: String
        get() = "Intern"

    // Implementing the abstract method to calculate salary
    override fun calculateSalary(): Double {
        return hourlyRate * hoursWorked // Total pay based on hours worked
    }
}

fun main() {
    // Creating instances of Manager, Developer, and Intern
    val manager = Manager("Alice", 35, 80000.0)
    val developer = Developer("Bob", 28, 60000.0, 5)
    val intern = Intern("Charlie", 22, 20.0, 100)

    // Using base class function to describe employees
    manager.describe()
    developer.describe()
    intern.describe()

    // Calculating and printing salaries for each employee
    println("Salary of ${manager.name} (${manager.position}): $${manager.calculateSalary()}")
    println("Salary of ${developer.name} (${developer.position}): $${developer.calculateSalary()}")
    println("Salary of ${intern.name} (${intern.position}): $${intern.calculateSalary()}")
}
