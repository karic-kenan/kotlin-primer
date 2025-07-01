package misc

import kotlin.collections.drop
import kotlin.collections.find
import kotlin.collections.forEach
import kotlin.collections.joinToString
import kotlin.collections.toTypedArray
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaMethod

object Reflection {

    // The base Character class for our game
    class GameCharacter(
        val name: String,
        var level: Int,
        var health: Int,
        var mana: Int,
        private var experience: Int = 0
    ) {
        fun attack(target: String): Int {
            val damage = level * 5
            println("$name attacks $target for $damage damage!")
            return damage
        }

        fun heal(amount: Int) {
            health += amount
            println("$name heals for $amount points. Current health: $health")
        }

        fun castSpell(spellName: String): Boolean {
            val manaCost = spellName.length * 2
            return if (mana >= manaCost) {
                mana -= manaCost
                println("$name casts $spellName! Remaining mana: $mana")
                true
            } else {
                println("$name doesn't have enough mana to cast $spellName!")
                false
            }
        }

        private fun gainExperience(amount: Int) {
            experience += amount
            println("$name gains $amount experience points! Total: $experience")
            checkLevelUp()
        }

        private fun checkLevelUp() {
            val experienceNeeded = level * 1000
            if (experience >= experienceNeeded) {
                level++
                experience -= experienceNeeded
                println("$name levels up to level $level!")
            }
        }

        override fun toString(): String {
            return "GameCharacter(name='$name', level=$level, health=$health, mana=$mana)"
        }
    }

    // Basic class inspection demonstration
    fun inspectGameCharacterClass() {
        println("=== Class Inspection ===")

        // Get class reference
        val characterClass = GameCharacter::class

        // Basic class information
        println("Class name: ${characterClass.simpleName}")
        println("Fully qualified name: ${characterClass.qualifiedName}")
        println("Is class abstract: ${characterClass.isAbstract}")
        println("Is class final: ${characterClass.isFinal}")
        println("Is class open: ${characterClass.isOpen}")
        println("Is class sealed: ${characterClass.isSealed}")
        println("Is class data: ${characterClass.isData}")

        // List all constructors
        println("\nConstructors:")
        characterClass.constructors.forEach { constructor ->
            val params = constructor.parameters.joinToString(", ") {
                "${it.name}: ${it.type}"
            }
            println("Constructor(${params})")
        }

        // List all properties
        println("\nProperties:")
        characterClass.memberProperties.forEach { property ->
            println("${property.name}: ${property.returnType} (visibility: ${property.visibility})")
        }

        // List all functions
        println("\nFunctions:")
        characterClass.memberFunctions.forEach { function ->
            val params = function.parameters.drop(1).joinToString(", ") {
                "${it.name}: ${it.type}"
            }
            println("${function.name}($params): ${function.returnType}")
        }

        println()
    }

    // Creating instances using reflection
    fun createCharacterWithReflection() {
        println("=== Creating Objects with Reflection ===")

        // Get the class reference and primary constructor
        val characterClass = GameCharacter::class
        val constructor = characterClass.primaryConstructor

        if (constructor != null) {
            // Prepare parameters for the constructor
            val parameters = listOf("Gandalf", 5, 100, 150, 2500)

            // Create a new instance dynamically
            val character = constructor.call(*parameters.toTypedArray())
            println("Created character: $character")

            // Create another character with different parameters
            val warrior = constructor.call("Aragorn", 7, 200, 50, 4000)
            println("Created warrior: $warrior")
        } else {
            println("Primary constructor not found!")
        }

        println()
    }

    // Accessing and modifying properties with reflection
    fun manipulateProperties() {
        println("=== Property Access and Modification ===")

        // Create a character instance
        val character = GameCharacter("Merlin", 3, 80, 200)
        println("Initial character: $character")

        // Get a reference to a property
        val levelProperty = GameCharacter::level

        // Read the property value
        val currentLevel = levelProperty.get(character)
        println("Current level: $currentLevel")

        // Modify the property value
        levelProperty.set(character, currentLevel + 1)
        println("After level change: $character")

        // Try to access a private property
        try {
            val experienceProperty = GameCharacter::class.memberProperties.find { it.name == "experience" }

            if (experienceProperty != null) {
                // Make private property accessible
                experienceProperty.javaField?.isAccessible = true

                // Now we can get its value
                val experienceValue = experienceProperty.get(character)
                println("Current experience (private field): $experienceValue")
            } else {
                println("Experience property not found")
            }
        } catch (e: Exception) {
            println("Error accessing private property: ${e.message}")
        }

        println()
    }

    // Invoking methods with reflection
    fun invokeMethods() {
        println("=== Method Invocation ===")

        // Create a character instance
        val character = GameCharacter("Harry", 5, 100, 120)
        println("Character: $character")

        // Get a reference to a method
        val attackMethod = GameCharacter::class.memberFunctions.find { it.name == "attack" }

        if (attackMethod != null) {
            // Invoke the method
            val damage = attackMethod.call(character, "Dragon")
            println("Attack resulted in $damage damage")
        }

        // Find and invoke another method
        val castSpellMethod = GameCharacter::class.memberFunctions.find { it.name == "castSpell" }

        if (castSpellMethod != null) {
            val spellSuccess = castSpellMethod.call(character, "Fireball")
            println("Spell casting success: $spellSuccess")
        }

        // Try to access a private method
        try {
            val gainExperienceMethod = GameCharacter::class.memberFunctions.find { it.name == "gainExperience" }

            if (gainExperienceMethod != null) {
                // Make private method accessible
                gainExperienceMethod.javaMethod?.isAccessible = true

                // Now we can invoke it
                gainExperienceMethod.call(character, 500)
            } else {
                println("gainExperience method not found")
            }
        } catch (e: Exception) {
            println("Error invoking private method: ${e.message}")
        }

        println()
    }

    // A dynamic ability system using reflection
    // Annotation to mark methods as abilities
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class GameAbility(val manaCost: Int, val description: String)

    // Extended GameCharacter with annotated ability methods
//    class EnhancedGameCharacter(
//        name: String,
//        level: Int,
//        health: Int,
//        var mana: Int
//    ) : GameCharacter(name, level, health, mana) {
//
//        @GameAbility(manaCost = 15, description = "Shoots a fireball at the target")
//        fun fireball(target: String): Int {
//            val damage = level * 10
//            println("$name casts Fireball at $target for $damage damage!")
//            return damage
//        }
//
//        @GameAbility(manaCost = 25, description = "Freezes the target in place")
//        fun frostNova(target: String) {
//            println("$name casts Frost Nova, freezing $target!")
//        }
//
//        @GameAbility(manaCost = 5, description = "Creates a magical light")
//        fun light() {
//            println("$name creates a magical light!")
//        }
//
//        // Non-ability method
//        fun jump() {
//            println("$name jumps high in the air!")
//        }
//    }

    // Ability manager class that uses reflection
//    class AbilityManager {
//        // Discover all abilities in a character
//        fun discoverAbilities(character: EnhancedGameCharacter): Map<String, KFunction<*>> {
//            val abilities = mutableMapOf<String, KFunction<*>>()
//
//            // Get all methods with the GameAbility annotation
//            character::class.memberFunctions.forEach { function ->
//                val annotation = function.findAnnotation<GameAbility>()
//                if (annotation != null) {
//                    abilities[function.name] = function
//                }
//            }
//
//            return abilities
//        }
//
//        // List all abilities and their descriptions
//        fun listAbilities(character: EnhancedGameCharacter) {
//            println("=== ${character.name}'s Abilities ===")
//
//            character::class.memberFunctions.forEach { function ->
//                val annotation = function.findAnnotation<GameAbility>()
//                if (annotation != null) {
//                    println("${function.name}: ${annotation.description} (Mana cost: ${annotation.manaCost})")
//                }
//            }
//
//            println()
//        }
//
//        // Cast an ability by name
//        fun castAbility(character: EnhancedGameCharacter, abilityName: String, vararg args: Any): Any? {
//            val ability = character::class.memberFunctions.find {
//                it.name == abilityName && it.findAnnotation<GameAbility>() != null
//            }
//
//            if (ability != null) {
//                val annotation = ability.findAnnotation<GameAbility>()
//
//                if (annotation != null) {
//                    // Check if character has enough mana
//                    if (character.mana >= annotation.manaCost) {
//                        // Deduct mana cost
//                        character.mana -= annotation.manaCost
//
//                        // Build parameter list with character as receiver
//                        val params = listOf(character) + args.toList()
//
//                        // Cast the ability
//                        println("Casting $abilityName (Mana cost: ${annotation.manaCost})")
//                        return ability.call(*params.toTypedArray())
//                    } else {
//                        println("Not enough mana to cast $abilityName!")
//                        return null
//                    }
//                }
//            }
//
//            println("Ability $abilityName not found!")
//            return null
//        }
//    }

    // Demonstrating the dynamic ability system
//    fun demonstrateAbilitySystem() {
//        println("=== Dynamic Ability System Demonstration ===")
//
//        // Create our enhanced character
//        val wizard = EnhancedGameCharacter("Dumbledore", 10, 150, 200)
//        println("Created wizard: $wizard with ${wizard.mana} mana")
//
//        // Create our ability manager
//        val abilityManager = AbilityManager()
//
//        // List all available abilities
//        abilityManager.listAbilities(wizard)
//
//        // Cast some abilities
//        abilityManager.castAbility(wizard, "fireball", "Dragon")
//        println("Remaining mana: ${wizard.mana}")
//
//        abilityManager.castAbility(wizard, "light")
//        println("Remaining mana: ${wizard.mana}")
//
//        abilityManager.castAbility(wizard, "frostNova", "Troll")
//        println("Remaining mana: ${wizard.mana}")
//
//        // Try to cast an ability that doesn't exist
//        abilityManager.castAbility(wizard, "teleport")
//
//        // Try to cast when we don't have enough mana
//        wizard.mana = 3
//        abilityManager.castAbility(wizard, "fireball", "Goblin")
//
//        println()
//    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("===== Kotlin Reflection Demonstration =====\n")

        // Run all demonstration functions
        inspectGameCharacterClass()
        createCharacterWithReflection()
        manipulateProperties()
        invokeMethods()
//        demonstrateAbilitySystem()

        println("===== Demonstration Complete =====")
    }
}
