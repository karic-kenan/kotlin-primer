### Introduction

Imagine you're building an app to manage users' profiles. You need to keep track of each user's first name, last name, age, and address. Instead of handling this data with just variables scattered across your code, wouldn't it be more efficient to group related data and behaviors together in one place? This is where classes in Kotlin come into play.

Classes are the building blocks of modern app development. They let us create organized, reusable blueprints that package related data and behaviors together. Think of a class like a template for creating objects - similar to how architects use blueprints before constructing actual buildings. When we model real-world entities like users, products, or transactions as classes, our code becomes easier to read, debug, test, and update as our app evolves.

By the end of this session, you'll understand:

- What classes are and how they form the foundation of Kotlin programming
- How to define properties and methods that bring your classes to life
- The role of constructors in setting up your objects correctly
- How access modifiers let you control who can see and use different parts of your class
- How `init` blocks help with complex object setup
- Some practical tips and common pitfalls to avoid when working with classes

---

### What are Classes?
A class in Kotlin is essentially a blueprint that describes what an object will look like and how it will behave. When we create a class, we're not creating an actual object yet - we're defining what properties (data) and methods (behaviors) that type of object will have.

Let's relate this to something familiar. Think about your smartphone. Before it was manufactured, engineers created detailed specifications describing its components, features, and behaviors. That specification is like a class, while your actual physical phone is like an object (or instance) of that class.

Classes weren't always part of programming languages. As software grew more complex in the 1970s, developers needed better ways to organize code. Languages like Simula and Smalltalk pioneered the object-oriented approach we now use in Kotlin, making it easier to build and maintain large software systems by grouping related functionality together.

---

### Class syntax
#### 1. Definition (`class ClassName`)
Creating a class starts with the `class` keyword followed by the name you choose:

```kotlin
class User { // Properties and methods go here }
```

This might look simple, but it's powerful. With just this declaration, we've created a new type in our program.

#### 2. Properties (`var`, `val`)
Properties are the characteristics or attributes that describe our class. In our User example, these might include:

```kotlin
class User {
    var firstName: String = ""
    var lastName: String = ""
    val id: Int = 0
    var age: Int = 0
}
```

Notice I've used both `var` and `val` here. What's the difference? Well, think of `var` as creating changeable characteristics - like age, which increases over time. On the other hand, `val` creates immutable properties - things that shouldn't change after creation, like a person's ID number.

#### 3. Methods (`fun methodName`)
While properties describe what an object is, methods define what an object can do. Methods are simply functions that belong to a class:

```kotlin
class User {
    // Properties
    var firstName: String = ""
    var lastName: String = ""
    
    // Method
    fun getFullName(): String {
        return "$firstName $lastName"
    }
}
```

This `getFullName()` method combines the first and last name properties to return the user's complete name. Methods help our objects interact with the rest of our program in a controlled way.

---

### Why do we need Classes?

- **Modularity**:
    - They help break down complex systems into smaller, manageable pieces. Each class represents a specific part of your system.
- **Encapsulation**:
    - Classes package data and behavior together, hiding internal details and exposing only what's necessary through a defined interface. This makes your code more secure and robust.
- **Reusability**:
    - Once you define a class, you can reuse it throughout your program or even in different projects, reducing duplicate code.
- **Inheritance and Polymorphism**:
    - Classes support these powerful object-oriented principles that enable code flexibility and reuse.
- **Abstraction**:
    - Classes let you model real-world entities at a higher level, without getting bogged down in implementation details.
- **Maintainability**:
    - When your code is organized into classes, updating or debugging one part of your system often won't affect other parts.

---

### Practical examples

#### 1. Fields/Properties

Fields are the variables that hold the data for a class. Let's create a simple `Person` class with some basic fields.

I'll start by adding a descriptive comment at the top.

```kotlin
// Fields are the properties that store data in a class
```

Now, let's define our Person class.

```kotlin
class Person {
```

First, let's add some mutable properties using `var`. These can be changed after the object is created.

```kotlin
    var firstName: String = ""  // mutable, can be changed
    var lastName: String = ""   // mutable, can be changed
```

Next, let's add an immutable property using `val`. Once set, this can't be changed.

```kotlin
    val age: Int = 0           // immutable, can't be changed once set
}
```

Notice how each property has a name, a type, and an initial value. The `var` properties can be modified later, but the `val` property is fixed after initialization.

#### 2. Custom getters and setters

Custom getters and setters allow us to control how properties are accessed and modified.

Let's create a more sophisticated version of our Person class with custom getters and setters.

```kotlin
class Person {
```

First, I'll add our basic properties.

```kotlin
    var firstName: String = ""
    var lastName: String = ""
```

Now, I'll create a `fullName` property with a custom getter and setter.

```kotlin
    var fullName: String
```

For the getter, I'll return a combination of firstName and lastName.

```kotlin
        get() = "$firstName $lastName"  // custom getter combines two properties
```

And for the setter, I'll parse the incoming value and update firstName and lastName accordingly.

```kotlin
        set(value) {                     // custom setter splits the input
            val parts = value.split(" ")
            if (parts.size == 2) {
                firstName = parts[0]
                lastName = parts[1]
            }
        }
}
```

This is really powerful. When someone reads `person.fullName`, they get the combined first and last names. When they set `person.fullName = "John Smith"`, our setter automatically updates the first and last name properties.

##### Why use custom getters and setters?
- **Encapsulation**: Custom getters and setters encapsulate the logic for accessing and modifying properties.
- **Validation**: They allow you to add validation logic when setting property values.
- **Transformation**: They enable transforming data when getting or setting a property.

#### 3. Constructors

Constructors initialize our objects when they're created. Kotlin offers primary and secondary constructors.

Let's first see what a primary constructor looks like.

```kotlin
// Primary constructor - part of the class header
```

The primary constructor goes right in the class header:

```kotlin
class Person(val firstName: String, val lastName: String, val age: Int) {
    // class body
}
```

Now let's see a class with both primary and secondary constructors.

```kotlin
// Class with primary and secondary constructors
```

Here's our primary constructor with just the name properties:

```kotlin
class Person(val firstName: String, val lastName: String) {
```

We'll add a mutable age property with a default value:

```kotlin
    var age: Int = 0
```

Now let's add a secondary constructor that also accepts an age parameter:

```kotlin
    constructor(firstName: String, lastName: String, age: Int) : this(firstName, lastName) {
```

Inside the secondary constructor, we assign the age parameter to our age property:

```kotlin
        this.age = age
    }
}
```

The `this(firstName, lastName)` part is crucial - it calls the primary constructor first before executing the secondary constructor's body.

#### 4. Access modifiers

Access modifiers control the visibility of class members.

Let's discuss the various access modifiers we can use in Kotlin:

```kotlin
// Access modifiers control visibility of class members:
// - public (default): accessible everywhere
// - private: only within the class
// - protected: within class and subclasses
// - internal: within the same module
```

Now, let's create a class demonstrating these modifiers:

```kotlin
class User {
```

Here's a public property (public is the default, so we don't need to specify it):

```kotlin
    val username: String = "default"  // public by default
```

A private property that can only be accessed within this class:

```kotlin
    private val password: String = "secret"
```

A protected property that's accessible in this class and its subclasses:

```kotlin
    protected val userLevel: Int = 1
```

And an internal property that's accessible within the same module:

```kotlin
    internal val lastLoginDate: String = "2023-01-01"
}
```

These modifiers help us control access to our class members, which is a key aspect of encapsulation in object-oriented programming.

### Example

Now let's put everything together by building a small library management system. We'll create classes for books, library members, and the library itself.

#### 1. Class definition: `Book`

Let's start by defining our Book class. I'll add a descriptive comment first.

```kotlin
// Book class - represents a book in our library
```

Now I'll define the class with its properties in the primary constructor.

```kotlin
class Book(
    val title: String,           // Public immutable property
    private val author: String,  // Private immutable property
    var pages: Int               // Public mutable property
) {
```

I'll add a private property to track if the book is available for borrowing:

```kotlin
    private var isAvailable: Boolean = true  // Private mutable property with default value
```

#### 2. Method: `borrow`
Now I'll add a method to borrow the book.

```kotlin
    // Public method to borrow the book
    fun borrow(): Boolean {
```

First, we check if the book is available:

```kotlin
        return if (isAvailable) {
```

If it is, we update its status, print a message, and return true:

```kotlin
            isAvailable = false
            println("Book '$title' has been borrowed")
            true
```

Otherwise, we print an error message and return false:

```kotlin
        } else {
            println("Sorry, '$title' is not available")
            false
        }
    }
```

#### 3. Method: `returnBook`
Next, let's add a method to return the book:

```kotlin
    // Public method to return the book
    fun returnBook() {
```

We mark the book as available again and print a confirmation:

```kotlin
        isAvailable = true
        println("Book '$title' has been returned")
    }
```

#### 4. Method: `getDetails`
Finally, let's add a method to get the book's details:

```kotlin
    // Public method to get book details
    fun getDetails(): String {
```

We'll check if the book is available and format it as a status:

```kotlin
        val status = if (isAvailable) "Available" else "Borrowed"
```

Then return a formatted string with all the book's details:

```kotlin
        return "Title: $title, Author: $author, Pages: $pages, Status: $status"
    }
}
```

With our Book class complete, we can create and manipulate book objects with clear behaviours for borrowing, returning, and viewing details.

---
#### 5. Class Definition: `Member`

Now let's create our Member class to represent library patrons.

```kotlin
// Member class - represents a library member
```

I'll define the class with basic properties in the primary constructor:

```kotlin
class Member(
    var firstName: String,           // Public mutable property
    var lastName: String,            // Public mutable property
    private val memberId: String     // Private immutable property
) {
```

Let's add a property for maximum books allowed, with a default value:

```kotlin
    private val maxBooksAllowed: Int = 5  // Private constant-like property
```

And a list to track which books this member has borrowed:

```kotlin
    private val borrowedBooks = mutableListOf<Book>()  // Private mutable collection
```

#### 6. Backing Field: `fullName`

Now I'll create a property with custom getter and setter for the member's full name:

```kotlin
    // Backing field with custom getter and setter
    var fullName: String
```

The getter will combine `firstName` and `lastName`:

```kotlin
        get() = "$firstName $lastName"  // Custom getter returns computed value
```

And the setter will split the provided name into first and last name components:

```kotlin
        private set(value) {  // Private setter - can only be changed within the class
            val names = value.split(" ")
            if (names.size == 2) {
                firstName = names[0]
                lastName = names[1]
            }
        }
```

#### 7. Lateinit property: `address`

Next, I'll add a `lateinit` property for the member's address:

```kotlin
    // Lateinit property - will be initialized later
    lateinit var address: String
```

The `lateinit` modifier is used for properties that are not initialized at the time of object creation but will be initialized later. It can only be used with mutable properties (`var`).

Using `lateinit` is useful when:
- The property cannot be initialized at the time of object creation.
- The property is guaranteed to be initialized before its first use.
- **`lateinit var address`**: Declares a mutable property that will be initialized later, after the object is created.

#### 8. Secondary constructor

Let's add a secondary constructor that allows setting the address during member creation:

```kotlin
    // Secondary constructor that also sets the address
    constructor(firstName: String, lastName: String, memberId: String, address: String) : this(
        firstName,
        lastName,
        memberId
    ) {
```

After the primary constructor is called, we set the address property:

```kotlin
        this.address = address
    }
```

#### 9. Init block

Now I'll add an initialization block that runs when a new Member is created:

```kotlin
    // Init block - runs when the object is instantiated
    init {
        println("New member created: $fullName with Member ID: $memberId")
    }
```

This code runs right after the primary constructor, regardless of which constructor was used to create the object.

#### 10. Method: `borrowBook`

Let's add a method for borrowing books:

```kotlin
    // Public method to borrow a book
    fun borrowBook(book: Book) {
```

First, we check if the member has reached their borrowing limit:

```kotlin
        if (borrowedBooks.size >= maxBooksAllowed) {
            println("$fullName cannot borrow more than $maxBooksAllowed books.")
            return
        }
```

If they haven't reached the limit, we try to borrow the book:

```kotlin
        if (book.borrow()) {
```

If successful, we add it to their borrowed books list and confirm:

```kotlin
            borrowedBooks.add(book)
            println("$fullName has borrowed '${book.title}'.")
        }
    }
```

#### 11. Method: `returnBook`

Now for the method to return a book:

```kotlin
    // Public method to return a book
    fun returnBook(book: Book) {
```

We try to remove the book from the member's borrowed books list:

```kotlin
        if (borrowedBooks.remove(book)) {
```

If found, we mark the book as returned and confirm:

```kotlin
            book.returnBook()
            println("$fullName has returned '${book.title}'.")
        } else {
```

If not found, we show an error message:

```kotlin
            println("$fullName does not have '${book.title}'.")
        }
    }
```

#### 12. Method: `updateAddress`

Let's add a method to update the member's address:

```kotlin
    // Public method to update address
    fun updateAddress(address: String) {
```

We'll check if the address was previously initialized:

```kotlin
        if (!this::address.isInitialized) {
            this.address = address
        }
```

Then update it and confirm:

```kotlin
        println("Address updated for $fullName: $address")
    }
```

#### 13. Method: `getMemberDetails`

Finally, let's add a method to get the member's full details:

```kotlin
    // Public method to get member details
    fun getMemberDetails(): String {
```

We'll check if the address is initialized and format it accordingly:

```kotlin
        return "Member ID: $memberId, Name: $fullName, " +
                "Address: ${if (::address.isInitialized) address else "Not provided"}, " +
                "Borrowed Books: ${borrowedBooks.size}"
    }
}
```

This method gives us a complete overview of the member, handling the case where the address might not be set yet.

---
#### 14. Class definition: `Library`

Now let's create our Library class to tie everything together.

```kotlin
// Library class - manages books and operations
```

I'll define the class with a name and a collection to store books:

```kotlin
class Library(
    private val name: String,
    private val books: MutableList<Book> = mutableListOf()
) {
```

#### 15. Method: `addBook`

Let's add a method to add books to the library:

```kotlin
    // Method to add a book to the library
    fun addBook(book: Book) {
```

We'll add the book to our collection and confirm:

```kotlin
        books.add(book)
        println("Book '${book.title}' added to the library.")
    }
```

#### 16. Method: `listBooks`

Finally, let's add a method to display all books in the library:

```kotlin
    // Method to list all books
    fun listBooks() {
```

We'll print a header with the library name:

```kotlin
        println("Books available in $name:")
```

Then iterate through each book and print its details:

```kotlin
        books.forEach { println(it.getDetails()) }
    }
}
```

With this method, we can see all the books in our library at any time.

#### 17. Main function: putting it all together

Now let's write a main function to demonstrate how these classes work together.

```kotlin
fun main() {
```

First, we'll create a new library:

```kotlin
    // Create a library
    val library = Library("Central Library")
    println("Created new library: Central Library")
```

Next, let's create some books and add them to the library:

```kotlin
    // Add books to the library
    val book1 = Book("The Great Gatsby", "F. Scott Fitzgerald", 180)
    val book2 = Book("1984", "George Orwell", 328)
    library.addBook(book1)
    library.addBook(book2)
```

Let's list all the books to see what's available:

```kotlin
    // List all books
    library.listBooks()
```

Now we'll create a library member:

```kotlin
    // Create a member
    val member = Member("John", "Doe", "M001", "123 Maple Street")
    println("Full name: ${member.fullName}")
```

Let's have the member borrow some books:

```kotlin
    // Member borrows books
    member.borrowBook(book1)
    member.borrowBook(book2)
```

Let's check the library again to see which books are available:

```kotlin
    // List all books again to see availability
    library.listBooks()
```

Now let's have the member return a book:

```kotlin
    // Return a book
    member.returnBook(book1)
```

Let's update the member's address:

```kotlin
    // Update member address
    member.updateAddress("456 Oak Avenue")
```

Finally, let's print the member's details:

```kotlin
    // Get member details
    println(member.getMemberDetails())
}
```

This main function demonstrates the complete workflow of our library system, from creating books and members to borrowing and returning books.

---

### Best practices and pitfalls

Here are some tips to make your classes more effective:

- **Encapsulate your data**:
    - Use `private` or `protected` modifiers to hide implementation details. This prevents others from accidentally changing your object's state in harmful ways.
- **Prefer `val` over `var`**:
    - Make properties unchangeable (using `val`) whenever possible. This creates more predictable code that's easier to reason about.
- **Provide default values**:
    - When it makes sense, give constructor parameters default values so creating objects is more flexible:
    
    ```kotlin
    class User(val name: String, val isPremium: Boolean = false)
    ```
- **Use `lateinit` carefully**:
    - Only use it when you're absolutely sure the property will be initialized before use. It's not suitable for primitive types or nullable properties.
- **Leverage custom getters/setters**:
    - They're perfect for computed properties or when you need validation:
    
    ```kotlin
    var email: String = ""
        set(value) {
            if (value.contains('@')) field = value
            else throw IllegalArgumentException("Invalid email")
        }
    ```
- **Keep classes focused**:
    - Each class should do one thing well, not try to solve many problems. This makes your code more modular and testable.

---

### Conclusion

Classes in Kotlin give you the power to create well-organized, maintainable code by grouping related data and behaviours together. By encapsulating your data properly, using constructors effectively, and following these best practices, you'll build applications that are not only functional but also flexible and easy to extend.

Remember, good class design isn't just about making your code work - it's about making it work well over time as your application grows and requirements change. The time you invest in learning these concepts now will pay dividends throughout your programming career.