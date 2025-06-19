#### Introduction

Hello! Welcome back to our Kotlin journey. Now that we've explored what makes Kotlin special and set up our development environment, it's time for the moment you've been waiting for - writing your first actual Kotlin program!

Today, we'll create a classic "Hello, World!" application, explore the project structure that IntelliJ IDEA created for us, and learn how to run our code both in the IDE and using the command line. By the end of this lesson, you'll have written, compiled, and executed your very first piece of Kotlin code - how exciting is that?

---

### Section 1: Understanding the Project Structure

Before we start typing, let's take a quick tour of what we're looking at in our new Kotlin project. Understanding this structure will really help you as your projects grow more complex.

#### 1.1 Exploring the Project Layout in IntelliJ

If you followed along with our last lesson, you should have IntelliJ IDEA open with a new Kotlin project. Take a look at the Project panel on the left side - you'll notice several files and directories that were automatically generated.

At the very top is your **project root** - this is the main container for everything related to your project. Inside, you'll see a few important items:

The **src directory** is where all your actual code will live. Think of this as the heart of your project. Inside src, you'll typically find two main folders:

- **main**: This holds all the code that makes up your actual application
- **test**: This is where you'd write code to test your application (we'll explore testing in a future lesson)

Under main, you'll find the **kotlin directory** - this is your home base where all the Kotlin files will go.

You might also notice some configuration files in your project root:

- If you're using Gradle, you'll see **build.gradle.kts** and **settings.gradle.kts**
- If you're using Maven, you'll see a **pom.xml** file

These files manage your project's dependencies and build settings - we'll talk more about those in future lessons when we start using external libraries.

#### 1.2 The Resource Directory

One other folder worth mentioning is the **resources directory**. This is where you store non-code files your application might need - things like configuration files, images, or data files. We won't need it for our simple example today, but it's good to know it's there.

---

### Section 2: Writing Your First Kotlin Program

Alright, now for the fun part - let's write some actual Kotlin code!

#### 2.1 Creating a New Kotlin File

First, we need to create a new Kotlin file:

1. Right-click on that **kotlin** directory we just talked about
2. Select **New > Kotlin File/Class**
3. Let's name our file **Main**

Perfect! You should now see a new file called **Main.kt** open in the editor. The ".kt" extension is for Kotlin files, just like ".java" is for Java files.

Now, let's add our first Kotlin code. Type or paste the following:

```kotlin
fun main(args: Array<String>) {
    println("Hello, World!")
}
```

Let's break down what we just wrote:

- **fun** is the keyword used to declare a function in Kotlin - it's much shorter than Java's "public static void"!
- **main** is the name of our function. This is special - it's the entry point where your program begins executing.
- **args: Array</String/>** is a parameter that allows your program to accept command-line arguments. We won't use it today, but it's traditionally included.
- Inside the curly braces is our function body - the actual code that runs.
- **println** is a function that prints text to the console, followed by a line break.

That's it! With just two lines of code, we've created a complete Kotlin program that displays a message. Notice how concise this is compared to the equivalent Java code - this is one of the things developers love about Kotlin.

#### 2.2 Running Your Program in IntelliJ

Now let's run our program and see what happens:

1. Look for the green "play" button in the gutter next to your main function
2. Click it and select "Run 'MainKt'"

You should see a Run window appear at the bottom of IntelliJ with our output: "Hello, World!"

Congratulations! You've just executed your first Kotlin program. That was pretty easy, right?

#### 2.3 Using the Command Line

While the IDE is convenient, it's also important to know how to compile and run Kotlin code from the command line. This is useful for scripting, automation, or when you're working in environments without an IDE.

Open a terminal or command prompt and navigate to your project directory. Then follow these steps:

1. **Compile your Kotlin file:**
    
    ```
    kotlinc src/main/kotlin/Main.kt -include-runtime -d Main.jar
    ```
    
    Let me explain what this command does:
    - **kotlinc** invokes the Kotlin compiler
    - **src/main/kotlin/Main.kt** is the path to our source file
    - **-include-runtime** bundles the Kotlin runtime library with our code so it can run independently
    - **-d Main.jar** specifies the output file name
2. **Run the compiled program:**
    
    ```
    java -jar Main.jar
    ```
    
    You should see the same "Hello, World!" output in your terminal.

This process highlights Kotlin's Java interoperability - behind the scenes, Kotlin compiles to Java bytecode that runs on the JVM. This is why you can easily mix Kotlin and Java in the same project.

---

### Section 3: Common Issues and Best Practices

As you start writing more Kotlin code, here are a few tips to keep in mind:

If you encounter compilation errors, double-check your syntax. Remember that Kotlin is case-sensitive, so `fun` and `Fun` are not the same thing.

For command-line compilation, don't forget the `-include-runtime` flag when you want to create a standalone JAR file.

A few best practices to start developing:

- Use meaningful file names that reflect what your code does
- Take advantage of IntelliJ's code formatting (Code > Reformat Code) to keep your code clean and consistent
- Get comfortable with the project structure early - it will make navigation much easier as your projects grow

---

### Conclusion
And there you have it! You've written, compiled, and run your first Kotlin program. Though it's a simple example, it introduces core concepts like functions, the println statement, and the project structure.

In our next lesson, we'll explore Kotlin's basic syntax in more depth and start looking at variables, data types, and expressions. We'll also introduce the Kotlin REPL - an interactive tool that lets you experiment with code quickly.

Feel free to play around with your "Hello, World!" program - try printing different messages or adding multiple println statements to see what happens.

I hope you're enjoying your Kotlin journey so far! See you in the next lesson.