package basics

import java.math.BigDecimal
import java.math.BigInteger

fun main() {
    // Variables
    val driverName: String = "Kimi Räikkönen"
    println("Driver name: $driverName")
    // driverName = "Max Verstappen" // This would cause a compilation error

    var carModel: String = "Tesla Model S"
    println("Initial car model: $carModel")
    carModel = "Tesla Model X" // This works fine with var
    println("Updated car model: $carModel")

    // Type inference
    val carMake = "Tesla"  // Kotlin infers this is a String
    val speed = 150.5      // Kotlin infers this is a Double
    println("Car make: $carMake, Speed: $speed km/h")
    var carPrice = 50000  // Inferred as Int
    // carPrice = "Expensive" // Error: Type mismatch

    // Integer types
    val maxSpeed: Byte = 127                // 8 bits, -128 to 127
    val mileage: Short = 15000              // 16 bits, -32,768 to 32,767
    val productionYear: Int = 2021          // 32 bits, about ±2 billion
    val totalProduction: Long = 5000000L    // 64 bits, very large range
    println("Max speed: $maxSpeed, Mileage: $mileage, Production year: $productionYear, Total production: $totalProduction")

    // Floating-point types
    val batteryCapacity: Float = 75.5F   // Notice the 'F' suffix
    val price: Double = 79999.99         // No suffix needed
    println("Battery capacity: $batteryCapacity kWh, Price: $$price")

    // Hexadecimal and binary literals
    val hexColorCode = 0xFFFFFF  // Hexadecimal value for white color
    val binaryFlag = 0b1010      // Binary representation
    println("Hex color code: $hexColorCode, Binary flag: $binaryFlag")

    // Number superclass
    val anotherNumber: Number = 42
    println("Number: $anotherNumber")

    // Number conversions
    val priceInt = price.toInt()  // Converting Double to Int
    val mileageDouble = mileage.toDouble()  // Converting Short to Double
    println("Price as Int: $priceInt, Mileage as Double: $mileageDouble")

    // Underscores in numbers
    val oneMillion = 1_000_000  // Much easier to read than 1000000
    println("One million: $oneMillion")

    // Operations on numbers
    println("Addition: ${productionYear + 100}")
    println("Subtraction: ${productionYear - 10}")
    println("Multiplication: ${batteryCapacity * 2}")
    println("Division: ${price / 2}")
    println("Integer division: ${5 / 2}") // Results in 2, not 2.5
    println("Modulo: ${5 % 2}") // Remainder: 1

    var totalDistance = 100
    println("Initial distance: $totalDistance km")

    totalDistance += 50  // Same as: totalDistance = totalDistance + 50
    println("After += 50: $totalDistance km")
    totalDistance -= 30  // Same as: totalDistance = totalDistance - 30
    println("After -= 30: $totalDistance km")
    totalDistance *= 2   // Same as: totalDistance = totalDistance * 2
    println("After *= 2: $totalDistance km")
    totalDistance /= 2   // Same as: totalDistance = totalDistance / 2
    println("After /= 2: $totalDistance km")
    totalDistance %= 7   // Same as: totalDistance = totalDistance % 7
    println("After %= 7: $totalDistance km")

    // Increment and decrement
    println("Pre-increment: ${++totalDistance}")  // Increments first, then uses value
    println("Pre-decrement: ${--totalDistance}")  // Decrements first, then uses value
    println("Post-increment: ${totalDistance++}")  // Uses value first, then increments
    println("Value after post-increment: $totalDistance")
    println("Post-decrement: ${totalDistance--}")  // Uses value first, then decrements
    println("Value after post-decrement: $totalDistance")

    // Operations on bits
    val binary1 = 0b1100  // 12 in decimal
    val binary2 = 0b1010  // 10 in decimal
    println("AND: ${binary1 and binary2}")      // 1000 (8 in decimal)
    println("OR: ${binary1 or binary2}")        // 1110 (14 in decimal)
    println("XOR: ${binary1 xor binary2}")      // 0110 (6 in decimal)
    println("Shift left: ${binary1 shl 1}")     // 11000 (24 in decimal)
    println("Shift right: ${binary1 shr 1}")    // 0110 (6 in decimal)

    // BigDecimal & BigInteger
    println("0.1 + 0.2 with Double: ${0.1 + 0.2}")  // Shows ~0.30000000000000004
    val carPriceBigDecimal = BigDecimal("79999.99")
    val anotherCarPriceBigDecimal = BigDecimal(50000)
    println("BigDecimal addition: ${carPriceBigDecimal + anotherCarPriceBigDecimal}")

    val totalCarsProducedBigInteger = BigInteger.valueOf(5000000L)
    val anotherBigInteger = BigInteger("2500000")
    println("BigInteger addition: ${totalCarsProducedBigInteger + anotherBigInteger}")

    // Booleans, expressions, operations and equality
    val isElectric: Boolean = true
    val isDamaging: Boolean = false
    val isEnvironmentFriendly: Boolean = true

    println("Is the car electric? $isElectric")
    println("AND (true && true): ${isElectric && isEnvironmentFriendly}")  // true
    println("OR (true || false): ${isElectric || isDamaging}")            // true
    println("NOT (!true): ${!isElectric}")                               // false

    println("Equality (5 == 5): ${5 == 5}")                  // true
    println("Inequality (5 != 10): ${5 != 10}")              // true
    println("Greater than (10 > 5): ${10 > 5}")              // true
    println("Less than (5 < 10): ${5 < 10}")                 // true

    // Boolean expressions
    val speedCheck = speed > 100
    println("Speed check (speed > 100): $speedCheck")

    // Characters
    val grade: Char = 'A'
    println("Grade: $grade, Unicode: \u0041")

    // Strings and interpolation
    val carBrand = "Tesla"
    val model = "Model S"
    val description = """
        Car brand: $carBrand
        Model: $model
        Is electric: $isElectric
        Price: $price
    """.trimIndent()
    println(description)

    val name = "Kenan"
    val age = 30
    println("My name is $name and I am $age years old.")

    val discount = 10
    val finalPrice = price - (price * discount / 100)
    println("Original price: $$price, after $discount% discount: $${"%.2f".format(finalPrice)}")

    // Escape sequences
    val escapeExample = "Let's say: \"Hello, World\""
    println(escapeExample)
    println("Tab\tSpace")
    println("New line\nText")
    println("Single quote: ' ")
    println("Backslash: \\")

    // Constants
    println("Manufacturer: $MANUFACTURER")
}

const val MANUFACTURER: String = "Tesla, Inc."
