package functional

import functional.HOF.GameItem
import kotlin.collections.filter
import kotlin.collections.groupBy
import kotlin.collections.map

// Type aliases for cleaner code
typealias ItemFilter = (GameItem) -> Boolean
typealias ItemEnhancer = (GameItem) -> GameItem
typealias ItemClassifier = (GameItem) -> String

object HOF {
    // Define an in-game item data class
    data class GameItem(val name: String, val type: String, val basePower: Int)

    // Higher-order function that accepts other functions as parameters
    private fun processGameItems(
        items: List<GameItem>,
        filter: ItemFilter,
        enhance: ItemEnhancer,
        groupBy: ItemClassifier
    ): Map<String, List<GameItem>> {
        return items
            .filter(filter)    // Apply the filtering function
            .map(enhance)      // Apply the enhancement function
            .groupBy(groupBy)  // Apply the grouping function
    }

    // Functions that will be passed as parameters
    private fun isWeapon(item: GameItem): Boolean = item.type == "Weapon"
    private fun isPowerful(item: GameItem): Boolean = item.basePower > 40

    // Enhancement functions
    private fun addFirePower(item: GameItem): GameItem {
        return item.copy(name = "Fire ${item.name}", basePower = item.basePower + 20)
    }

    private fun addIcePower(item: GameItem): GameItem {
        return item.copy(name = "Ice ${item.name}", basePower = item.basePower + 15)
    }

    // Function that returns another function
    private fun createItemFilter(minPower: Int): ItemFilter {
        return { item -> item.basePower > minPower }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val items = listOf(
            GameItem("Sword", "Weapon", 50),
            GameItem("Shield", "Armor", 30),
            GameItem("Potion", "Consumable", 10),
            GameItem("Axe", "Weapon", 60),
            GameItem("Dagger", "Weapon", 35)
        )

        println("---- Basic Higher-Order Function Example ----")

        // Example 1: Using named functions as parameters
        val weaponsWithFirePower = processGameItems(
            items,
            filter = ::isWeapon,
            enhance = ::addFirePower,
            groupBy = { it.type }
        )
        println("Weapons with fire power: $weaponsWithFirePower")

        // Example 2: Using lambda expressions directly
        val powerfulItemsWithIce = processGameItems(
            items,
            filter = { it.basePower > 40 },
            enhance = ::addIcePower,
            groupBy = { it.type }
        )
        println("Powerful items with ice: $powerfulItemsWithIce")

        // Example 3: Combining predicates to create more complex filters
        val combinedFilter: ItemFilter = { item ->
            isWeapon(item) && isPowerful(item)
        }

        val powerfulWeaponsWithFire = processGameItems(
            items,
            filter = combinedFilter,
            enhance = ::addFirePower,
            groupBy = { it.type }
        )
        println("Powerful weapons with fire: $powerfulWeaponsWithFire")

        // Example 4: Using a function that returns another function
        println("\n---- Function That Returns a Function ----")
        val highPowerFilter = createItemFilter(50)
        val veryHighPowerItems = processGameItems(
            items,
            filter = highPowerFilter,
            enhance = ::addFirePower,
            groupBy = { it.type }
        )
        println("Very high power items: $veryHighPowerItems")

        // Example 5: Function stored in a variable
        println("\n---- Functions as Variables ----")
        val customEnhancer: ItemEnhancer = { item ->
            item.copy(
                name = "Enhanced ${item.name}",
                basePower = item.basePower * 2
            )
        }

        val enhancedItems = processGameItems(
            items,
            filter = { true },
            enhance = customEnhancer,
            groupBy = { it.type }
        )
        println("Custom enhanced items: $enhancedItems")
    }
}

