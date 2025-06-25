### Introduction

Imagine you're building an app that tracks everything about cars - speed, fuel efficiency, production details. You need to store the model name, price, production year, and more. How do you ensure each piece of data is stored efficiently and accessed accurately?

This is where variables come into play. They're fundamental building blocks in programming, allowing you to store, modify, and retrieve data dynamically. Whether you're developing a game, business application, or any software, variables enable your code to manage data effectively.

By the end of this lecture, you'll understand:
- The difference between mutable (`var`) and immutable (`val`) variables
- How Kotlin handles different data types
- The significance of type inference
- Practical examples of variables in arithmetic and bitwise operations
- Best practices for managing data with variables

---

### What are variables?

A variable is essentially a symbolic name associated with a value stored in the computer's memory. It's like a labelled container that holds data which can be referenced and manipulated throughout your program.

Variables have been fundamental since the early days of programming. Languages like Fortran in the 1950s introduced variables for numerical data, while COBOL expanded this to business data. Over decades, variables evolved to support increasingly complex data types, enhancing programming flexibility.

---

### Variables syntax

In Kotlin, you declare a variable using `var` or `val`. `var` is for mutable variables (those that can be changed), and `val` is for immutable variables (those that cannot be changed once assigned).

```kotlin
val variableName: Type = value
var variableName: Type = value

// Examples
val name: String = "Kenan"
var age: Int = 30
val carBrand: String = "Maybach"
```

**Name**: A variable must have a name, which is used to refer to the stored value. Variable names should be descriptive and follow certain naming conventions (e.g., camelCase, where the first word starts with a lowercase letter, and each subsequent word starts with an uppercase letter.).

**Type**: Kotlin is a statically-typed language, meaning each variable must be declared with a data type.
- **Primitive types:** Int, Double, Boolean, Char
- **Reference types:** String, Arrays, Collections (like List, Set, Map)
- **Custom types:** Classes and objects you define

**Value**: A variable stores a value that can be retrieved and modified (if it's mutable). The value must be compatible with the variable’s data type.

---

### Why do we need variables?
- **Data storage**:
    - Variables are essential for storing data that a program can use later. They allow you to save information such as numbers, text, and other data types that your program can manipulate or reference as needed. Without variables, you'd have to re-enter data every time it's needed, making programs inefficient and cumbersome.
- **Reusability**:
    - Variables enable you to reuse data throughout your program without having to hard-code values multiple times. This not only makes your code more concise but also reduces the risk of errors. If a value needs to change, you can simply update the variable, and the change will reflect wherever the variable is used.
- **Flexibility**:
    - By using variables, your programs can handle dynamic data that changes over time. For instance, user input, results from calculations, or data retrieved from external sources can be stored in variables, allowing your program to adapt to different situations and inputs.
- **Readability and maintenance**:
    - Variables make your code more readable by providing meaningful names to data values. This helps others (and your future self) understand what the data represents and how it’s being used. Well-named variables can significantly enhance the clarity and maintainability of your code.
- **Memory management**:
    - Variables also play a crucial role in managing memory within your program. They allow your program to allocate space in memory for data that can be accessed and manipulated efficiently. Without variables, managing memory manually would be much more complex and error-prone.

---

### Practical examples
Let's open IntelliJ IDEA and get our hands dirty with actual code. I'll create a new package called `basics` and inside it, a new Kotlin file named `VariablesTypesOperations`.
#### 1. `val` and `var`

**Concept**: Kotlin has two types of variables: immutable (`val`) and mutable (`var`).

First, let's look at `val` variables. Inside our `main` function I'll type a declaration for a driver name:

```kotlin
val driverName: String = "Kimi Räikkönen"
```

Now I'm adding a print statement to see our variable value:

```kotlin
println("Driver name: $driverName")
```

Let me add a commented-out line showing what would happen if we tried to change a `val`:

```kotlin
// driverName = "Max Verstappen" // This would cause a compilation error
```

Next, let's see how `var` works by creating a car model variable:

```kotlin
var carModel: String = "Tesla Model S"
```

I'll add a print statement to show the initial value:

```kotlin
println("Initial car model: $carModel")
```

Now with `var`, I can reassign a new value - watch this:

```kotlin
carModel = "Tesla Model X" // This works fine with var
```

And I'll print the updated value to show the change:

```kotlin
println("Updated car model: $carModel")
```

**Key insight**: Always use `val` unless you specifically need to change a variable's value later. This prevents accidental changes and makes your code more predictable.

#### 2. Type inference

**Concept**: Kotlin can automatically determine variable types from their assigned values.

Let's see type inference in action. I'll create variables without explicitly stating their types:

```kotlin
val carMake = "Tesla"  // Kotlin infers this is a String
```

And now a numeric value:

```kotlin
val speed = 150.5      // Kotlin infers this is a Double
```

I'll print both to show they work just like explicitly typed variables:

```kotlin
println("Car make: $carMake, Speed: $speed km/h")
```

Let's try another example with a variable that might need to change:

```kotlin
var carPrice = 50000  // Inferred as Int
```

Let me add a commented line showing what would happen if we tried to assign a different type:

```kotlin
// carPrice = "Expensive" // Error: Type mismatch
```

**Key Insight**: Type inference makes your code cleaner and more readable, but remember that Kotlin is still strongly typed - once a type is inferred, it's fixed.

#### 3. Integer types

**Concept**: Kotlin provides several integer types with different memory sizes for different needs (Byte, Short, Int, Long).

Let's explore Kotlin's integer types. I'll start with the smallest, Byte:

```kotlin
val maxSpeed: Byte = 127              // 8 bits, -128 to 127
```

Next, let's create a Short variable, which gives us a bit more range:

```kotlin
val mileage: Short = 15000            // 16 bits, -32,768 to 32,767
```

Int is the most commonly used integer type, so let's add that:

```kotlin
val productionYear: Int = 2021        // 32 bits, about ±2 billion
```

For really big numbers, we need Long. Notice I add the `L` suffix to tell Kotlin it's a Long:

```kotlin
val totalProduction: Long = 5000000L  // 64 bits, very large range
```

Let's print all these values to see them:

```kotlin
println("Max speed: $maxSpeed, Mileage: $mileage, Production year: $productionYear, Total production: $totalProduction")
```

**Key Insight**: Most of the time you'll just use `Int`, but knowing the other types helps when you need to optimize memory or handle very large numbers.

#### 4. Floating-point types

**Concept**: For decimal numbers, Kotlin offers two types with different precision levels (Float, Double).

Let's start with Float:

```kotlin
val batteryCapacity: Float = 75.5F   // Notice the 'F' suffix
```

For higher precision, we use Double, which is the default decimal type:

```kotlin
val price: Double = 79999.99         // No suffix needed
```

Now I'll print these values to see them:

```kotlin
println("Battery capacity: $batteryCapacity kWh, Price: $$price")
```

**Key Insight**: Use `Double` for most decimal calculations. Only use `Float` when you need to conserve memory and don't need high precision.

#### 5. Hexadecimal and Binary literals

**Concept**: Kotlin allows writing numbers in different formats beyond decimal (Hexadecimal, Binary).

Let's create a hexadecimal value:

```kotlin
val hexColorCode = 0xFFFFFF  // Hexadecimal value for white color
```

We can also define binary numbers directly:

```kotlin
val binaryFlag = 0b1010      // Binary representation
```

Let's print these values to see how Kotlin handles them:

```kotlin
println("Hex color code: $hexColorCode, Binary flag: $binaryFlag")
```

**Key Insight**: While they're displayed as decimal in output, these formats make your intentions clearer in code, especially for bit operations or colour values.

#### 6. Using Number superclass

**Concept**: All numeric types in Kotlin inherit from a common `Number` superclass, providing flexibility when working with different number types.

```
val anotherNumber: Number = 42  
println("Number: $anotherNumber")
```

While this flexibility is convenient, remember that you'll still need explicit conversions when performing operations between different number types. The `Number` superclass gives you type flexibility without losing Kotlin's type safety.

#### 7. Number conversions

**Concept**: Kotlin requires explicit conversion between number types.

Let me show you how to convert explicitly:

```kotlin
val priceInt = price.toInt()  // Converting Double to Int
```

And here's how to convert in the other direction:

```kotlin
val mileageDouble = mileage.toDouble()  // Converting Short to Double
```

Let's print these converted values:

```kotlin
println("Price as Int: $priceInt, Mileage as Double: $mileageDouble")
```

**Key Insight**: Explicit conversions prevent unexpected behavior but require you to be intentional about type changes.

#### 8. Using underscores in numbers

**Concept**: Kotlin allows underscores in numeric literals for better readability.

Let me show you how underscores make them clearer:

```kotlin
val oneMillion = 1_000_000  // Much easier to read than 1000000
```

Let's print it to confirm it works as a normal number:

```kotlin
println("One million: $oneMillion")
```

**Key Insight**: The underscores are ignored by the compiler - they're purely for human readability.

#### 9. Basic arithmetic operations

**Concept**: Kotlin supports all standard arithmetic operations with familiar syntax.

Let's see the basic arithmetic operators in action. I'll start with addition:

```kotlin
println("Addition: ${productionYear + 100}")
```

Now subtraction:

```kotlin
println("Subtraction: ${productionYear - 10}")
```

Multiplication is also straightforward:

```kotlin
println("Multiplication: ${batteryCapacity * 2}")
```

Division works as expected:

```kotlin
println("Division: ${price / 2}")
```

But be careful with integer division - it truncates the decimal part:

```kotlin
println("Integer division: ${5 / 2}") // Results in 2, not 2.5
```

And finally, the modulo operator gives us the remainder:

```kotlin
println("Modulo: ${5 % 2}") // Remainder: 1
```

**Key Insight**: With integer division, be aware that you'll lose the decimal portion. If you need a decimal result, make sure at least one operand is a decimal type.

#### 10. Compound assignment operators

**Concept**: Kotlin provides shorthand operators that combine arithmetic with assignment.

Let's start by creating a variable to track distance:

```kotlin
var totalDistance = 100
println("Initial distance: $totalDistance km")
```

The `+=` operator adds to our variable and assigns the result back to it:

```kotlin
totalDistance += 50  // Same as: totalDistance = totalDistance + 50
println("After += 50: $totalDistance km")
```

Similarly, `-=` subtracts and assigns:

```kotlin
totalDistance -= 30  // Same as: totalDistance = totalDistance - 30
println("After -= 30: $totalDistance km")
```

`*=` multiplies and assigns:

```kotlin
totalDistance *= 2   // Same as: totalDistance = totalDistance * 2
println("After *= 2: $totalDistance km")
```

`/=` divides and assigns:

```kotlin
totalDistance /= 2   // Same as: totalDistance = totalDistance / 2
println("After /= 2: $totalDistance km")
```

And `%=` calculates the remainder and assigns:

```kotlin
totalDistance %= 7   // Same as: totalDistance = totalDistance % 7
println("After %= 7: $totalDistance km")
```

**Key Insight**: These compound operators make your code more concise and often more readable when performing operations on the same variable.

#### 11. Increment and decrement operators

**Concept**: The `++` and `--` operators provide shortcuts for adding or subtracting 1.

First, the prefix version:

```kotlin
println("Pre-increment: ${++totalDistance}")  // Increments first, then uses value
```

And prefix decrement:

```kotlin
println("Pre-decrement: ${--totalDistance}")  // Decrements first, then uses value
```

With postfix increment, the order is reversed:

```kotlin
println("Post-increment: ${totalDistance++}")  // Uses value first, then increments
println("Value after post-increment: $totalDistance")
```

And finally, postfix decrement:

```kotlin
println("Post-decrement: ${totalDistance--}")  // Uses value first, then decrements
println("Value after post-decrement: $totalDistance")
```

**Key Insight**: The difference between prefix and postfix is subtle but important, especially in expressions or when the value is used multiple times.

#### 12. Bitwise operations

**Concept**: Kotlin provides operators for manipulating individual bits within numbers.

First, I'll define two binary numbers:

```kotlin
val binary1 = 0b1100  // 12 in decimal
val binary2 = 0b1010  // 10 in decimal
```

The `and` operator sets bits that are 1 in both numbers:

```kotlin
println("AND: ${binary1 and binary2}")      // 1000 (8 in decimal)
```

The `or` operator sets bits that are 1 in either number:

```kotlin
println("OR: ${binary1 or binary2}")        // 1110 (14 in decimal)
```

The `xor` sets bits that are 1 in one number but not both:

```kotlin
println("XOR: ${binary1 xor binary2}")      // 0110 (6 in decimal)
```

We can also shift bits left, which is like multiplying by 2:

```kotlin
println("Shift left: ${binary1 shl 1}")     // 11000 (24 in decimal)
```

Or shift right, which is like dividing by 2:

```kotlin
println("Shift right: ${binary1 shr 1}")    // 0110 (6 in decimal)
```

**Key Insight**: Bitwise operations are powerful for low-level programming, working with flags, or optimizing certain algorithms.

#### 13. High-precision numbers

**Concept**: For financial calculations or very large numbers, Kotlin offers specialized types.

Let's see why standard floating-point types can cause issues with precision:

```kotlin
println("0.1 + 0.2 with Double: ${0.1 + 0.2}")  // Shows ~0.30000000000000004
```

For precise decimal calculations, especially money, we should use `BigDecimal`:

```kotlin
val carPriceBigDecimal = BigDecimal("79999.99")
val anotherCarPriceBigDecimal = BigDecimal(50000)
println("BigDecimal addition: ${carPriceBigDecimal + anotherCarPriceBigDecimal}")
```

And for extremely large integers, we use `BigInteger`:

```kotlin
val totalCarsProducedBigInteger = BigInteger.valueOf(5000000L)
val anotherBigInteger = BigInteger("2500000")
println("BigInteger addition: ${totalCarsProducedBigInteger + anotherBigInteger}")
```

**Key Insight**: Always use `BigDecimal` for financial calculations to avoid the inherent rounding errors in floating-point arithmetic.

#### 14. Booleans and logical operations
**Concept**: Boolean variables store `true/false` values and can be combined with logical operators.

Let's create some boolean values for our car properties:

```kotlin
val isElectric: Boolean = true
val isDamaging: Boolean = false
val isEnvironmentFriendly: Boolean = true
```

I'll print one to see how it looks:

```kotlin
println("Is the car electric? $isElectric")
```

Now let's try logical AND (`&&`), which needs both conditions to be true:

```kotlin
println("AND (true && true): ${isElectric && isEnvironmentFriendly}")  // true
```

Logical OR (`||`) needs at least one condition to be true:

```kotlin
println("OR (true || false): ${isElectric || isDamaging}")            // true
```

And logical NOT (`!`) inverts a boolean value:

```kotlin
println("NOT (!true): ${!isElectric}")                               // false
```

We can also create boolean results with comparison operators:

```kotlin
println("Equality (5 == 5): ${5 == 5}")                  // true
println("Inequality (5 != 10): ${5 != 10}")              // true
println("Greater than (10 > 5): ${10 > 5}")              // true
println("Less than (5 < 10): ${5 < 10}")                 // true
```

And store these comparison results in variables:

```kotlin
val speedCheck = speed > 100
println("Speed check (speed > 100): $speedCheck")
```

**Key Insight**: Boolean expressions are the foundation of conditional logic and flow control in your programs.

#### 15. Characters and strings

**Concept**: Kotlin provides distinct types for single characters and sequences of characters.

Let's create a character first - note the single quotes for Char:

```kotlin
val grade: Char = 'A'
println("Grade: $grade, Unicode: \u0041")
```

For text data, we use String with double quotes:

```kotlin
val carBrand = "Tesla"
val model = "Model S"
```

Kotlin also supports multi-line strings with triple quotes:

```kotlin
val description = """
    Car brand: $carBrand
    Model: $model
    Is electric: $isElectric
    Price: $price
""".trimIndent()
println(description)
```

String interpolation (`$`) makes it easy to insert variables into strings:

```kotlin
val name = "Kenan"
val age = 30
println("My name is $name and I am $age years old.")
```

For expressions, we use curly braces:

```kotlin
val discount = 10
val finalPrice = price - (price * discount / 100)
println("Original price: $$price, after $discount% discount: $${"%.2f".format(finalPrice)}")
```

**Key Insight**: Strings in Kotlin are immutable - when you modify a string, you're actually creating a new string object.

#### 16. Escape sequences

**Concept**: In Kotlin, **escape sequences** allow us to insert special characters inside strings, such as quotation marks, new lines, tabs, and backslashes. This is useful when you need to format text correctly or include special symbols.

 **1. Handling Quotes in Strings**

If you need to include **double quotes** inside a string, you must escape them using a **backslash (`\`)** so Kotlin does not mistake them for the end of the string.

```kotlin
val escapeExample = "Let's say: \"Hello, World\""
println(escapeExample)
```

Here, the **`\"`** ensures the double quotes appear inside the string instead of closing it.

 **2. Tab Space (`\t`)**

 The `\t` escape sequence adds a **tab space**, which helps with text alignment.
 
```kotlin
println("Tab\tSpace")
```

The **tab (`\t`)** adds extra spacing, making the text easier to read.

 **3. New Line (`\n`)**

The `\n` escape sequence moves the text to the **next line**.

```kotlin
println("New line\nText")
```

The `\n` creates a line break, splitting the text into two lines.

 **4. Single Quote (`'`)**

Unlike double quotes, **single quotes (`'`) do not need escaping** inside a string.

```kotlin
println("Single quote: ' ")
```

Since single quotes are not used to define a string, they can be included **without escaping**.

 **5. Backslash (`\\`)**

Since **`\` (backslash)** is used for escape sequences, if you need to display an actual backslash in your text, you must escape it using **double backslashes (`\\`)**.

```kotlin
println("Backslash: \\")
```

The **first `\` escapes the second `\`**, allowing the backslash to appear correctly.

#### 17. Constants

**Concept**: For truly unchanging values, Kotlin provides compile-time constants.

Let's define a compile-time constant - note it must be at the top level of the file:

```kotlin
const val MANUFACTURER: String = "Tesla, Inc."
```

Now I can use this constant anywhere in my code:

```kotlin
println("Manufacturer: $MANUFACTURER")
```

**Key Insight**: Use `const val` for values known at compile time that truly never change, like mathematical constants or API keys.

---

### Best Practices and Pitfalls
- **Use descriptive names:**
    - Always choose meaningful and descriptive names for your variables. This makes your code more readable and maintainable. For example, instead of naming a variable `x`, name it `carModel` or `totalProduction`.
- **Use `val` whenever possible:**
    - Prefer using `val` for variables that don’t need to change after initialization. This helps ensure immutability, making your code safer and less prone to bugs.
- **Consistent naming conventions:**
    - Follow a consistent naming convention, such as camelCase for variable names (`carModel`, `totalProduction`). This helps maintain a uniform code style across your projects.
- **Limit variable scope:**
    - Declare variables in the smallest scope possible to reduce the risk of unintended side effects. Avoid declaring variables at a global or class level unless necessary.
- **Initialize variables properly:**
    - Always initialize your variables with a meaningful value before using them. This prevents runtime errors due to uninitialized variables.
- **Use constants for fixed values:**
    - When dealing with values that shouldn’t change, use constants (`const val`). This not only improves code readability but also ensures that these values remain consistent throughout your codebase.
- **Shadowing variables:**
	- Be cautious of variable shadowing, where a variable declared in a smaller scope has the same name as one in a larger scope. This can lead to confusion and bugs, as it’s easy to mistakenly reference the wrong variable.
- **Using magic numbers:**
	- Avoid using unexplained numerical values directly in your code (e.g., `val discount = price * 0.15`). Instead, assign them to descriptive variables or constants (`val discountRate = 0.15`) to make your code more understandable.

---

### Conclusion
Variables are essential for managing data in your applications. Whether storing user inputs, tracking state, or performing calculations, variables let you work with data dynamically and efficiently.

By understanding variable declaration, type inference, and best practices, you'll write cleaner, more maintainable code. Remember to choose between `val` and `var` thoughtfully, use meaningful names, and apply type inference wisely.