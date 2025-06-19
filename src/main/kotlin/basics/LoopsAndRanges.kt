package basics

fun main() {
    // Inventory of items in the grocery store
    val inventory = mapOf(
        "apple" to 50,
        "banana" to 30,
        "carrot" to 20,
        "date" to 10
    )

    // for loop -> inclusive range
    println("Checking stock levels (inclusive range):")
    for (stockLevel in 1..10) {
        println("Stock level: $stockLevel")
    }
    println()

    // for loop -> exclusive range using until
    println("Checking stock levels (exclusive range using 'until'):")
    for (stockLevel in 1 until 10) {
        println("Stock level: $stockLevel")
    }
    println()

    // for loop -> step
    println("Checking stock levels with step:")
    for (stockLevel in 1..20 step 5) {
        println("Stock level: $stockLevel")
    }
    println()

    // for loop -> descending range
    println("Checking stock levels (descending range):")
    for (stockLevel in 10 downTo 1) {
        println("Stock level: $stockLevel")
    }
    println()

    // for loop -> descending range with step
    println("Checking stock levels (descending range with step):")
    for (stockLevel in 20 downTo 1 step 5) {
        println("Stock level: $stockLevel")
    }
    println()

    // for loop -> loop over collections
    println("Looping over inventory items:")
    for (item in inventory.keys) {
        println("Item: $item, Stock: ${inventory[item]}")
    }
    println()

    // forEach for collections
    println("Using forEach to loop over inventory items:")
    inventory.forEach { (item, stock) ->
        println("Item: $item, Stock: $stock")
    }
    println()

    // for loop -> loop over array with indices
    println("Looping over inventory with indices:")
    val items = inventory.keys.toList()
    for (index in items.indices) {
        println("Item at index $index is ${items[index]}, Stock: ${inventory[items[index]]}")
    }
    println()

    // withIndex
    println("Using withIndex to loop over inventory items with indices:")
    inventory.keys.withIndex().forEach { (index, item) ->
        println("Item at $index is $item, Stock: ${inventory[item]}")
    }
    println()

    // repeat loop
    println("Using repeat to loop a fixed number of times:")
    repeat(5) {
        println("This is repetition number ${it + 1}")
    }
    println()

    // while loop
    println("Checking stock levels (while loop):")
    var i = 1
    while (i <= 10) {
        println("Stock level: $i")
        i += 1
    }
    println()

    // do-while loop
    println("Checking stock levels (do-while loop):")
    var j = 10
    do {
        println("Stock level: $j")
        j -= 1
    } while (j >= 1)
    println()

    // continue in loops
    println("Continue in loops:")
    for (stockLevel in 1..10) {
        if (stockLevel % 2 == 0) {
            continue
        }
        println("Stock level: $stockLevel")
    }
    println()

    // nested loops
    println("Nested loops for inventory and stock levels:")
    for (item in inventory.keys) {
        for (stockLevel in 1..3) {
            println("Item: $item, Stock level: $stockLevel")
        }
    }
    println()

    // breaking out of loops
    println("Breaking out of a loop:")
    for (stockLevel in 1..10) {
        if (stockLevel == 5) {
            break
        }
        println("Stock level: $stockLevel")
    }
    println()

    // breaking out of loops -> custom labels
    println("Breaking out of a loop using custom labels:")
    outerLoop@ for (item in inventory.keys) {
        for (stockLevel in 1..10) {
            if (inventory[item] == stockLevel) {
                println("Breaking out of a loop when item=$item and stockLevel=$stockLevel")
                break@outerLoop
            }
        }
    }
    println()

    // range with characters
    for (ch in 'a'..'e') {
        println("Character: $ch")
    }
    println()
}
