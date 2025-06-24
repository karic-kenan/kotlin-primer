package oop

// Base class Animal
open class Animal(
    val name: String,
    val species: String,
    val age: Int,
    private val isCarnivore: Boolean // New property to indicate dietary habits
) {
    // Method to describe the animal
    open fun describe() {
        println("$name is a $age-year-old $species.")
    }

    // Method to simulate feeding the animal
    open fun feed() {
        if (isCarnivore) {
            println("$name is being fed with meat.")
        } else {
            println("$name is being fed with plants.")
        }
    }

    // Method to make the animal sleep
    open fun sleep() {
        println("$name is sleeping.")
    }
}

// Derived class Lion inheriting from Animal
class Lion(name: String, age: Int) : Animal(name, "Lion", age, true) {

    // Overriding the describe method to add more details
    override fun describe() {
        super.describe()
        println("$name is a majestic carnivore and the king of the jungle.")
    }

    // Overriding the feed method to provide a specific diet
    override fun feed() {
        println("$name is being fed with fresh meat.")
    }

    // Specific method for lion
    fun roar() {
        println("$name is roaring loudly!")
    }
}

// Derived class Elephant inheriting from Animal
class Elephant(name: String, age: Int) : Animal(name, "Elephant", age, false) {

    // Overriding the describe method to add more details
    override fun describe() {
        super.describe()
        println("$name is a gentle giant and herbivore.")
    }

    // Overriding the feed method to provide a specific diet
    override fun feed() {
        println("$name is being fed with fruits and vegetables.")
    }

    // Specific method for elephant
    fun trumpet() {
        println("$name is trumpeting with its trunk!")
    }
}

// Derived class Zookeeper
class Zookeeper(val name: String, private val animals: MutableList<Animal> = mutableListOf()) {

    // Method to add an animal under the zookeeper's care
    fun addAnimal(animal: Animal) {
        animals.add(animal)
        println("$name is now taking care of ${animal.name}.")
    }

    // Method to feed all animals under the zookeeper's care
    fun feedAnimals() {
        animals.forEach { it.feed() }
    }

    // Method to put all animals to sleep
    fun putAnimalsToSleep() {
        animals.forEach { it.sleep() }
    }

    // Method to show the zookeeper's list of animals
    fun showAnimals() {
        println("$name is taking care of the following animals:")
        animals.forEach { it.describe() }
    }
}

fun main() {
    // Create instances of Lion and Elephant
    val lion = Lion("Simba", 5)
    val elephant = Elephant("Dumbo", 10)

    // Create an instance of Zookeeper
    val zookeeper = Zookeeper("Joe")

    // Add animals to the zookeeper's care
    zookeeper.addAnimal(lion)
    zookeeper.addAnimal(elephant)

    // Show the animals under care
    zookeeper.showAnimals()

    // Feed the animals
    zookeeper.feedAnimals()

    // Specific actions for each animal
    lion.roar()
    elephant.trumpet()

    // Put the animals to sleep
    zookeeper.putAnimalsToSleep()
}
