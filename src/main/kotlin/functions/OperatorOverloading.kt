package functions

import java.util.*
import kotlin.ranges.coerceIn
import kotlin.text.format
import kotlin.text.lowercase

class Color(private var red: Int, private var green: Int, private var blue: Int) {
    init {
        // Ensure color values are in valid range
        red = red.coerceIn(0, 255)
        green = green.coerceIn(0, 255)
        blue = blue.coerceIn(0, 255)
    }

    // ========== Arithmetic Operators ==========

    // Binary arithmetic operators
    operator fun plus(other: Color): Color =
        Color((red + other.red) / 2, (green + other.green) / 2, (blue + other.blue) / 2)

    operator fun plus(colorValue: Int): Color =
        Color(red + colorValue, green + colorValue, blue + colorValue)

    operator fun minus(other: Color): Color =
        Color(
            kotlin.comparisons.maxOf(0, red - other.red),
            kotlin.comparisons.maxOf(0, green - other.green),
            kotlin.comparisons.maxOf(0, blue - other.blue)
        )

    operator fun times(factor: Double): Color =
        Color((red * factor).toInt(), (green * factor).toInt(), (blue * factor).toInt())

    operator fun div(divisor: Int): Color =
        Color(red / divisor, green / divisor, blue / divisor)

    operator fun rem(other: Color): Color =
        Color(red % other.red, green % other.green, blue % other.blue)

    // ========== Compound Assignment Operators ==========

    operator fun plusAssign(colorValue: Int) {
        red = (red + colorValue).coerceIn(0, 255)
        green = (green + colorValue).coerceIn(0, 255)
        blue = (blue + colorValue).coerceIn(0, 255)
    }

    operator fun minusAssign(colorValue: Int) {
        red = (red - colorValue).coerceIn(0, 255)
        green = (green - colorValue).coerceIn(0, 255)
        blue = (blue - colorValue).coerceIn(0, 255)
    }

    operator fun timesAssign(factor: Double) {
        red = ((red * factor).toInt()).coerceIn(0, 255)
        green = ((green * factor).toInt()).coerceIn(0, 255)
        blue = ((blue * factor).toInt()).coerceIn(0, 255)
    }

    // ========== Increment & Decrement Operators ==========

    operator fun inc(): Color =
        Color(red + 1, green + 3, blue + 7)

    operator fun dec(): Color =
        Color(red - 1, green - 3, blue - 7)

    // ========== Unary Operators ==========

    operator fun unaryPlus(): Color =
        Color(red + 1, green, blue)

    operator fun unaryMinus(): Color =
        Color(255 - red, 255 - green, 255 - blue)  // More intuitive: inverts the color

    operator fun not(): Color =
        Color(255 - red, 255 - green, 255 - blue)  // Alternative way to invert

    // ========== Indexing Operators ==========

    operator fun get(index: Int): Int =
        when (index) {
            0 -> red
            1 -> green
            2 -> blue
            else -> throw kotlin.IllegalArgumentException("Color has only 3 color channels (0-2)")
        }

    operator fun get(index1: Int, index2: Int, index3: Int): Int {
        return get(index1) + get(index2) + get(index3)
    }

    operator fun get(channel: String): Int =
        when (channel.lowercase(Locale.getDefault())) {
            "red", "r" -> red
            "green", "g" -> green
            "blue", "b" -> blue
            else -> throw kotlin.IllegalArgumentException("Invalid color channel: use 'red'/'r', 'green'/'g', or 'blue'/'b'")
        }

    operator fun set(index: Int, value: Int) {
        when (index) {
            0 -> red = value.coerceIn(0, 255)
            1 -> green = value.coerceIn(0, 255)
            2 -> blue = value.coerceIn(0, 255)
            else -> throw kotlin.IllegalArgumentException("Color has only 3 color channels (0-2)")
        }
    }

    operator fun set(channel: String, value: Int) {
        when (channel.lowercase(Locale.getDefault())) {
            "red", "r" -> red = value.coerceIn(0, 255)
            "green", "g" -> green = value.coerceIn(0, 255)
            "blue", "b" -> blue = value.coerceIn(0, 255)
            else -> throw kotlin.IllegalArgumentException("Invalid color channel: use 'red'/'r', 'green'/'g', or 'blue'/'b'")
        }
    }

    // ========== Contains (in) Operator ==========

    operator fun contains(value: Int): Boolean =
        red == value || green == value || blue == value

    // ========== Comparison Operators ==========

    operator fun compareTo(other: Color): Int {
        // Compare by luminance (perceived brightness)
        val thisLuminance = 0.299 * red + 0.587 * green + 0.114 * blue
        val otherLuminance = 0.299 * other.red + 0.587 * other.green + 0.114 * other.blue
        return thisLuminance.compareTo(otherLuminance)
    }

    // ========== Destructuring Operators ==========

    operator fun component1() = red
    operator fun component2() = green
    operator fun component3() = blue

    // ========== Invoke Operator ==========

    operator fun invoke(color: Color): Color =
        this.plus(color)  // Reuse the plus operator logic

    operator fun invoke(brightness: Double): Color =
        this.times(brightness)  // Reuse the times operator logic

    // ========== Utility Methods ==========

    fun toHex(): String = String.format("#%02X%02X%02X", red, green, blue)

    override fun toString(): String = "rgb($red, $green, $blue)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Color) return false
        return red == other.red && green == other.green && blue == other.blue
    }

    override fun hashCode(): Int {
        var result = red
        result = 31 * result + green
        result = 31 * result + blue
        return result
    }

    companion object {
        // Factory methods for common colors
        val BLACK = Color(0, 0, 0)
        val WHITE = Color(255, 255, 255)
        val RED = Color(255, 0, 0)
        val GREEN = Color(0, 255, 0)
        val BLUE = Color(0, 0, 255)
    }
}

fun main() {
    println("===== Kotlin Operator Overloading Demo =====\n")
    val color1 = Color(100, 150, 200)
    val color2 = Color(50, 75, 100)

    println("Initial colors:")
    println("color1: $color1 | Hex: ${color1.toHex()}")
    println("color2: $color2 | Hex: ${color2.toHex()}\n")

    // Arithmetic operators
    println("===== Arithmetic Operators =====")
    println("color1 + color2 = ${color1 + color2}")
    println("color1 + 20 = ${color1 + 20}")
    println("color1 - color2 = ${color1 - color2}")
    println("color1 * 1.5 = ${color1 * 1.5}")
    println("color1 / 2 = ${color1 / 2}")
    println("color1 % Color(50, 100, 150) = ${color1 % Color(50, 100, 150)}")

    // Compound assignment
    println("\n===== Compound Assignment Operators =====")
    val mutableColor = Color(100, 150, 200)
    println("Original: $mutableColor")
    mutableColor += 30
    println("After += 30: $mutableColor")
    mutableColor -= 10
    println("After -= 10: $mutableColor")
    mutableColor *= 1.2
    println("After *= 1.2: $mutableColor")

    // Increment/decrement
    println("\n===== Increment/Decrement Operators =====")
    val incColor = color1.inc()
    println("color1++: $incColor")
    val decColor = color1.dec()
    println("color1--: $decColor")

    // Unary operators
    println("\n===== Unary Operators =====")
    println("+color1: ${+color1}")
    println("-color1: ${-color1}")
    println("!color1: ${!color1}")

    // Indexing
    println("\n===== Indexing Operators =====")
    println("color1[0]: ${color1[0]}")  // Red channel
    println("color1[1]: ${color1[1]}")  // Green channel
    println("color1[2]: ${color1[2]}")  // Blue channel
    println("color1[\"red\"]: ${color1["red"]}")
    println("color1[\"g\"]: ${color1["g"]}")  // Short form

    // Setting values
    println("\n===== Set Operators =====")
    val settableColor = Color(100, 150, 200)
    println("Original: $settableColor")
    settableColor[0] = 200
    println("After settableColor[0] = 200: $settableColor")
    settableColor["b"] = 50
    println("After settableColor[\"b\"] = 50: $settableColor")

    // Contains
    println("\n===== Contains (in) Operator =====")
    println("150 in color1: ${150 in color1}")
    println("42 in color1: ${42 in color1}")

    // Comparison
    println("\n===== Comparison Operators =====")
    val darkColor = Color(30, 40, 50)
    val lightColor = Color(200, 210, 220)
    println("darkColor < lightColor: ${darkColor < lightColor}")
    println("darkColor > lightColor: ${darkColor > lightColor}")
    println("darkColor <= color1: ${darkColor <= color1}")
    println("color1 >= lightColor: ${color1 >= lightColor}")

    // Destructuring
    println("\n===== Destructuring =====")
    val (r, g, b) = color1
    println("Destructured color1: r=$r, g=$g, b=$b")

    // Invoke
    println("\n===== Invoke Operator =====")
    println("color1(color2): ${color1(color2)}")  // Mix colors
    println("color1(0.5): ${color1(0.5)}")  // Darken by 50%

    // Equals (not an overloadable operator, but important to implement)
    println("\n===== Equals Method =====")
    val sameAsColor1 = Color(100, 150, 200)
    println("color1 == sameAsColor1: ${color1 == sameAsColor1}")
    println("color1 == color2: ${color1 == color2}")

    // Using companion object constants
    println("\n===== Using Predefined Colors =====")
    println("Color.RED: ${Color.RED}")
    println("Color.WHITE: ${Color.WHITE}")
}
