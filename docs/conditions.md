### Introduction

Imagine walking into your favorite grocery store, ready to buy some fresh apples. You check your wallet to see how much money you have, then glance at the stock on the shelves. Are there enough apples left? Can you afford to buy them? Maybe there's even a special offer today that could save you some money. This is where conditionals come into play.

In app development with Kotlin, understanding and implementing conditions is crucial because they enable your app to make decisions and respond dynamically to different inputs or scenarios. Whether you're validating user input on a form, determining which screen to display next, or deciding which data to fetch from a server, conditions form the backbone of these decisions. By mastering conditional statements, you'll be able to create apps that are not only more interactive but also more personalized and responsive to user behavior.

By the end of this lecture, you will understand:
- What conditions are in programming and their role in Kotlin
- How to use `if`, `else if`, `else`, and `when` statements effectively
- The significance of conditions in controlling program flow and decision-making
- Practical examples of applying conditionals in real-world scenarios
- Best practices for writing clear, maintainable conditional logic

---

### What are Conditionals?
Conditionals are programming constructs that allow your app to make decisions and execute specific code blocks based on whether certain conditions are met. These conditions are typically Boolean expressions that evaluate to either `true` or `false`. Depending on the outcome, your program can choose different paths of execution.

This concept dates back to the earliest days of programming in the 1950s. While initial decision-making constructs were basic, modern languages like Kotlin offer sophisticated and flexible conditionals that help you write clean, readable code.

---

### Conditions syntax
Kotlin gives us two primary ways to handle conditions:
#### 1. The `if` Statement

The `if` statement executes code when a condition is true. It's the foundation of decision-making in imperative programming:

```kotlin
if (condition) {
    // Code executed when condition is true
} else {
    // Code executed when condition is false
}
```

#### 2. The `when` Statement

The `when` statement evaluates a value against multiple conditions. It's Kotlin's more powerful alternative to the traditional `switch` statement:

```kotlin
when (expression) {
    value1 -> { ... } // Code for value1
    value2 -> { ... } // Code for value2
    else -> { ... } // Default code
}
```

---

### Why do we need conditions?
Let's talk about why conditional statements are essential in programming:

- **Decision Making**:
    - Without conditionals, our programs would follow a single, rigid sequence of instructions. Conditions allow our code to make choices and adapt to different situations.
- **Dynamic Behaviour**:
    - Conditionals enable our programs to react to different inputs or situations at runtime. This makes our apps versatile and responsive to a wide range of scenarios.
- **Control Flow Management**:
    - Statements like `if` and `when` help manage the execution flow in a program, ensuring that only relevant code runs based on specific conditions.
- **Error Handling and Validation**:
    - We use conditionals to validate data and handle errors, like checking if a value is null or if a user has entered correct input.
- **Simplifying Complex Logic**:
    - The `when` statement is particularly useful for simplifying complex decision-making processes in a clean, readable format.
- **Enhanced Readability and Maintenance**:
    - Well-structured conditionals clearly define decision points in your program, making your code easier to understand and maintain.

---

### Practical examples
Alright, now let’s dive into some real-world examples to understand how **`if`** and **`when`** statements work in Kotlin.

#### 1. Basic If-Else Structure

First, let's add couple of variables to your main function, so we have something to work with:

```kotlin
val itemName = "Apples"
val itemStock = 50
val itemPrice = 1.99
val customerBudget = 10.00
```

Next, let's look at the most basic form of conditions in Kotlin - the if-else statement. 

```kotlin
// Imperative programming example
if (itemStock > 0) {  
    println("$itemName are available in stock.")  
} else {  
    println("$itemName are out of stock.")  
}
```

In this example, we're checking if `itemStock` is greater than 0. If it is, we print that the item is available in stock. Otherwise, we let the user know it's out of stock. This is the most fundamental way of making decisions in your code, and you'll use this pattern frequently in your programming.

#### 2. If as an Expression

Now, here's something cool about Kotlin—`if` statements can be used as expressions, meaning they can return a value that we can assign to a variable.

```kotlin
// Expression evaluated for different conditions
val stockStatus: String = if (itemStock > 50) {  
    "High stock of $itemName"  
} else if (itemStock in 20..50) {  
    "Moderate stock of $itemName"  
} else {  
    "Low stock of $itemName"  
}  
println(stockStatus)
```

Instead of just printing messages, we're using an `if` expression to determine how much stock we have. If there's more than 50 items, it sets 'High stock'. If we have between 20 and 50 items, it's 'Moderate stock'. Otherwise, it's 'Low stock'. Then we print this status. This approach makes our code more concise and readable.

#### 3. Nested Conditionals

Sometimes we need to make decisions within decisions. For this, we use nested `if` statements.

```kotlin
// Nested if statements
val purchaseStatus: String = if (customerBudget >= itemPrice) {  
    if (customerBudget >= itemPrice * 10) {  
        "Customer can buy up to 10 $itemName"  
    } else {  
        "Customer can buy some $itemName"  
    }  
} else {  
    "Customer cannot afford $itemName"  
}  
println(purchaseStatus)
```

Imagine a customer wants to buy something. First, we check if they can afford even a single item. If they can't, we set the message to 'Customer cannot afford' the item. If they can afford it, we then check if they have enough money to buy 10 items. Based on that, we set the appropriate message. While powerful, be careful not to nest too deeply, as it can make your code harder to follow.

#### 4. Basic When Statement

Now let's explore Kotlin's `when` statement, which is like a much improved version of the traditional `switch` statement you might have seen in other languages.

```kotlin
// when -> improved "switch"
val dayOfWeek = "Monday"  
  
when (dayOfWeek) {  
    "Monday" -> println("10% off on $itemName")  
    "Wednesday" -> println("Buy 1 Get 1 Free on $itemName")  
    "Friday" -> println("20% off on all items")  
    else -> println("No special offers today")  
}
```

Here, we're checking the day of the week and applying different promotional offers based on the result. If it's Monday, we offer a 10% discount. On Wednesday, we have a 'Buy One Get One Free' deal. On Friday, everything is 20% off. For any other day, there's no special offer. This makes it really easy to handle multiple different cases without a bunch of if-else statements.

#### 5. When with Multiple Conditions per Branch

One great feature of `when` is that you can group multiple values in a single branch.

```kotlin
// Multiple conditions in a single when branch
when (dayOfWeek) {  
    "Saturday", "Sunday" -> println("Store is open from 10 AM to 4 PM")  
    else -> println("Store is open from 9 AM to 8 PM")  
}
```

For example, here we check if it's Saturday or Sunday. If it is either of those days, the store has shorter hours. Otherwise, it's open for regular business hours. This is cleaner and more readable than writing multiple `if` conditions separately.

#### 6. When Returning a Value

Just like `if`, the `when` statement can also return a value.

```kotlin
// Returning a value from when
val orderQuantity = 15

val orderStatus = when (orderQuantity) {  
    in 1..10 -> "Small order"  
    in 11..50 -> "Medium order"  
    else -> "Large order"  
}  
println(orderStatus)
```

Here, we're checking the size of an order. Based on the `orderQuantity`, we classify it as a 'Small order' if it's between 1-10 items, 'Medium order' if it's between 11-50 items, or 'Large order' if it's more than that. Since `when` returns a value, we can directly assign it to a variable—clean and efficient!

#### 7. When Without an Argument

Now, let's look at another way to use `when`—without an argument!

```kotlin
// Using when without an argument
val customerAge = 25

val ageGroup = when {  
    customerAge < 18 -> "Minor"  
    customerAge in 18..64 -> "Adult"  
    customerAge >= 65 -> "Senior"  
    else -> "Unknown age group"  
}  
println(ageGroup)
```

Instead of checking a single variable, we can write conditions directly inside each branch, similar to `if-else` chains. Here, we check the customer's age and categorize them as 'Minor' if they're under 18, 'Adult' if they're between 18 and 64, or 'Senior' if they're 65 or older. This is a very flexible way to handle multiple conditions without explicitly comparing a single variable.

#### 8. When with Expressions in Branches

Here's something interesting: `when` can evaluate expressions inside its branches.

```kotlin
// Using expressions in when branches  
val discountEligibility = when (customerAge) {  
    16 + 9 -> "Eligible for adult discount"  
    else -> "Not eligible for special discount"  
}  
println(discountEligibility)
```

In this case, we check if the customer's age is 16 + 9 (which is 25). If it matches, they get an 'Eligible for adult discount' message. Otherwise, they don't get any special discount. This shows how you can use math expressions inside a `when` statement just like you would in `if`.

#### 9. Type Checking with When

Now, let's talk about something really powerful—type checking using `when`.

```kotlin
// Checking the type with when  
val customerFeedback: Any = "Great service!"

val feedbackType = when (customerFeedback) {  
    is Int -> "Feedback is a rating number"  
    is String -> "Feedback is a comment"  
    else -> "Unknown feedback type"  
}  
println(feedbackType)
```

Here, we have a variable called `customerFeedback`, which could be any type. If it's an `Int`, we assume it's a rating (like 5 stars). If it's a `String`, we assume it's a comment (like 'Great service!'). Otherwise, it's an 'Unknown feedback type'. This is super useful when working with dynamic data where you might not always know the type in advance.

---

### Best Practices and Pitfalls
To write effective conditional code in Kotlin, keep these best practices in mind:

* **Keep conditions simple and readable**
   - Break complex conditions into smaller, named functions or variables to improve clarity.
* **Use `when` instead of multiple `if-else` chains**
    - When you have several possible outcomes, use `when` instead of long chains of `if-else` statements.
* **Use `if` as an Expression for assignments**
    - Take advantage of Kotlin's ability to use `if` for direct value assignments.
* **Avoid deeply nested conditions**
    - Deep nesting makes code hard to follow. Consider using early returns or extracting functions instead.
* **Use `when` without an argument for complex logic**
    - When dealing with multiple unrelated conditions, use a `when` statement without an argument.
* **Use constants or enums for clearer conditionals**
   - Instead of magic numbers or strings, use constants or Enums in your conditions to make the code more understandable and maintainable.

---

### Conclusion
Mastering conditionals in Kotlin is essential for controlling how your applications behave in different scenarios. With `if` statements for simple decisions and the versatile `when` statement for complex cases, you have powerful tools to handle various situations effectively.

By applying the best practices we've discussed, you'll write cleaner, more efficient code that responds intelligently to different inputs and scenarios. Remember, well-structured conditional logic makes your programs not only more functional but also easier to read and maintain.