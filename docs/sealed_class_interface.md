### Introduction

Imagine you're developing a music streaming application where you need to handle different types of media content - songs, podcasts, audiobooks, and live broadcasts. Each type has unique attributes and behaviors, but they all represent audio content. Now imagine you need to process these types differently throughout your app. You could use regular inheritance with a base class, but how would you ensure you've handled every possible type in your code? What if someone adds a new content type later? This is where sealed classes come into play - they let you create restricted class hierarchies where you know exactly what subclasses exist, making your code safer and more maintainable.

Sealed classes and interfaces represent a powerful feature in Kotlin that bridges the gap between enumerations and unrestricted inheritance hierarchies. They provide compile-time safety by restricting the set of possible subclasses, which helps the compiler verify that you've handled all possible cases in your code. This is particularly valuable for representing restricted hierarchies like data models, state machines, or return types where you want to ensure exhaustive handling. Sealed hierarchies combine the flexibility of polymorphism with the safety of closed type sets, giving you the best of both worlds. By using sealed classes and interfaces, you create more robust code that's resistant to many common errors and easier to maintain as requirements evolve.

Here's what we'll cover today:

- What sealed classes and interfaces are and their role in Kotlin programming
- How sealed hierarchies compare to enums and open class hierarchies
- How to define and implement sealed classes and interfaces
- Using sealed hierarchies with the Kotlin `when` expression for exhaustive checks
- Creating nested sealed hierarchies for complex domain modeling
- The visibility and inheritance rules that govern sealed hierarchies
- Practical use cases through our media content example
- Best practices for designing clean, focused sealed hierarchies
- Common pitfalls to watch out for when working with sealed classes and interfaces

### What are sealed classes and interfaces?

A **sealed class** or **sealed interface** in Kotlin is a restricted class hierarchy where all direct subclasses must be defined within the same file as the sealed parent or within the same module as nested classes. Unlike open classes, which anyone can extend from anywhere, sealed classes explicitly define a closed set of subclasses, allowing the compiler to know all possible types that can exist in that hierarchy. Think of them as saying, "These are the only possible implementations of this type that can exist in my program." This gives you the safety of enumerations but with the flexibility of full classes that can contain state and behavior.

Sealed classes emerged as a solution to the pattern matching challenges faced in functional programming languages. Languages like Scala introduced sealed classes in the early 2000s, allowing for exhaustive pattern matching on class hierarchies. Kotlin adopted sealed classes in its early versions, but initially with limitations - all subclasses had to be nested within the sealed parent. In Kotlin 1.1, this restriction was relaxed to allow subclasses in the same file, and more recently, Kotlin 1.5 introduced sealed interfaces, expanding the concept further. These developments reflect the growing understanding of the importance of restricted hierarchies in creating safe, maintainable code, particularly in domains where representing a finite set of variations is essential.

### Sealed class/interface syntax

#### 1. Sealed class definition (`sealed class ClassName`)

Creating a sealed class starts with the `sealed` keyword followed by the `class` keyword and the name you choose:

```kotlin
sealed class MediaContent { // Properties and methods go here }
```

This declaration creates a sealed hierarchy where all direct subclasses must be defined in the same file or as nested classes. The sealed class itself is abstract by default - you cannot create instances of it directly.

#### 2. Sealed interface definition (`sealed interface InterfaceName`)

Since Kotlin 1.5, you can also create sealed interfaces:

```kotlin
sealed interface MediaAction { // Methods go here }
```

Like sealed classes, sealed interfaces restrict where their implementations can be defined. However, interfaces still follow interface rules - they cannot store state and can only declare abstract or default methods.

#### 3. Creating subclasses

You define subclasses of a sealed class or implementations of a sealed interface within the same file:

```kotlin
sealed class MediaContent {
    // Common properties and methods
    abstract val title: String
    abstract val durationSeconds: Int
}

// Direct subclasses defined in the same file
class Song(
    override val title: String,
    override val durationSeconds: Int,
    val artist: String,
    val album: String
) : MediaContent()

class Podcast(
    override val title: String,
    override val durationSeconds: Int,
    val host: String,
    val episode: Int
) : MediaContent()
```

The subclasses can be regular classes (`class`), data classes (`data class`), or even other sealed classes (`sealed class`).

#### 4. Nested subclasses

Alternatively, you can define subclasses as nested classes within the sealed parent:

```kotlin
sealed class MediaContent {
    abstract val title: String
    abstract val durationSeconds: Int
    
    data class Song(
        override val title: String,
        override val durationSeconds: Int,
        val artist: String,
        val album: String
    ) : MediaContent()
    
    data class Podcast(
        override val title: String,
        override val durationSeconds: Int,
        val host: String,
        val episode: Int
    ) : MediaContent()
}
```

This approach keeps the entire hierarchy in one place, making it easier to understand the complete set of possible types.

#### 5. Using with `when` expression

One of the key benefits of sealed classes is exhaustive handling with the `when` expression:

```kotlin
fun getMediaDescription(content: MediaContent): String {
    return when (content) {
        is MediaContent.Song -> "Song: ${content.title} by ${content.artist}"
        is MediaContent.Podcast -> "Podcast: ${content.title}, Episode ${content.episode}"
        // No else branch needed - the compiler knows all possible subclasses
    }
}
```

The compiler verifies that all subclasses are handled, eliminating the need for an `else` branch and ensuring your code remains correct even as the hierarchy evolves.

#### 6. Sealed interfaces with multiple implementations

Classes can implement multiple interfaces, including sealed ones:

```kotlin
sealed interface Playable {
    fun play()
}

sealed interface Downloadable {
    fun download()
}

class PlayableContent(val name: String) : Playable, Downloadable {
    override fun play() {
        println("Playing $name")
    }
    
    override fun download() {
        println("Downloading $name")
    }
}
```

This allows for more flexible modeling compared to sealed classes, which follow single-inheritance rules.

### Why do we need sealed classes/interfaces?

Sealed classes and interfaces solve several important problems in programming:

- **Exhaustiveness checking:**
    - The most significant benefit is compile-time verification that you've handled all possible cases. When using a sealed hierarchy with a `when` expression, the compiler ensures you've covered all subclasses, preventing runtime errors from missed cases.
- **Restricted hierarchies:**
    - Sometimes you want to define a fixed set of subclasses for a type, especially for representing domain concepts like states, events, or results. Sealed classes enforce this restriction while still allowing polymorphism.
- **Pattern matching with safety:**
    - Sealed hierarchies enable safe pattern matching, where you can check the specific subtype and access its unique properties, knowing you've accounted for every possibility.
- **Expression-based return types:**
    - Sealed classes are particularly useful for representing return types that can have multiple forms, like success or various error cases. They're more expressive and type-safe than using generic error handling approaches.
- **Documentation and discoverability:**
    - By keeping all subclasses together (either in the same file or nested), sealed hierarchies make it easier for other developers to understand all possible variations of a type, improving code readability and maintainability.
- **Evolution with safety:**
    - When you add a new subclass to a sealed hierarchy, the compiler will flag all `when` expressions that need to be updated, ensuring your code stays correct as your model evolves.

### Practical examples

#### 1. Defining a base sealed class for media content

Let's start by creating our sealed class hierarchy to represent different types of media content in our streaming application.

Sealed classes in Kotlin start with the `sealed` keyword followed by the `class` keyword and the name.

```kotlin
sealed class MediaContent {
```

Now I'll define the common properties that all types of media content must have. Unlike interfaces, sealed classes can contain state and concrete implementations.

```kotlin
    abstract val title: String
    abstract val durationSeconds: Int
    
    // Common method to format duration as minutes:seconds
    fun formattedDuration(): String {
        val minutes = durationSeconds / 60
        val seconds = durationSeconds % 60
        return "$minutes:${seconds.toString().padStart(2, '0')}"
    }
}
```

I've included an abstract property for the title and duration that all subclasses must implement, plus a concrete method that formats the duration nicely. This demonstrates how sealed classes can provide both required contracts and shared functionality.

#### 2. Creating subclasses for specific media types

Now I'll create our specific media content types as subclasses of our sealed class. Since these are direct subclasses of a sealed class, they must be defined in the same file.

I'll use a data class for our Song type since it's primarily about holding data.

```kotlin
data class Song(
    override val title: String,
    override val durationSeconds: Int,
    val artist: String,
    val album: String,
    val genre: String
) : MediaContent() {
```

Notice how I've overridden the abstract properties from the parent class and added song-specific properties like artist, album, and genre.

```kotlin
    // Song-specific method
    fun artistAndAlbum(): String = "$artist - $album"
}
```

I've also added a song-specific method that combines the artist and album information.

Next, let's create another media type - Podcast.

Again, I'll use a data class since it's primarily about holding data.

```kotlin
data class Podcast(
    override val title: String,
    override val durationSeconds: Int,
    val host: String,
    val episode: Int,
    val showName: String
) : MediaContent() {
```

I've overridden the required properties and added podcast-specific properties like host, episode number, and show name.

```kotlin
    // Podcast-specific method
    fun episodeInfo(): String = "Episode $episode of $showName"
}
```

Let's add one more media type - Audiobook.

Again using a data class for consistency.

```kotlin
data class Audiobook(
    override val title: String,
    override val durationSeconds: Int,
    val author: String,
    val narrator: String,
    val chapters: List<String>
) : MediaContent() {
```

I've implemented the required properties and added audiobook-specific properties.

```kotlin
    // Audiobook-specific method
    fun chapterCount(): Int = chapters.size
}
```

#### 3. Demonstrating exhaustive when expressions

Now let's see one of the key benefits of sealed classes - exhaustive checking with `when` expressions. I'll create a function that generates a description based on the media type.

This function takes any MediaContent and returns a formatted description string.

```kotlin
fun getMediaDescription(content: MediaContent): String {
```

Here's where the magic happens - I'll use a when expression to handle each possible type.

```kotlin
    return when (content) {
```

For each subclass, I can safely cast using 'is' and access its specific properties.

```kotlin
        is Song -> "${content.title} by ${content.artist} from album ${content.album}"
        is Podcast -> "${content.title} - ${content.episodeInfo()}"
        is Audiobook -> "${content.title} by ${content.author}, narrated by ${content.narrator}"
        // No else branch needed! The compiler knows these are all possible cases
    }
}
```

Notice that I don't need an 'else' branch here. The compiler knows all possible subtypes of MediaContent and ensures I've handled them all. If I were to add another subclass later, this function would not compile until I updated it to handle the new case.

#### 4. Creating a sealed interface for media actions

Now let's implement a sealed interface to represent different actions that can be performed on media content.

Similar to sealed classes, sealed interfaces start with the 'sealed' keyword followed by 'interface'.

```kotlin
sealed interface MediaAction {
    // All implementing classes will have to provide this information
    val actionName: String
    val requiresNetwork: Boolean
}
```

Unlike sealed classes, interfaces can't have constructor parameters or concrete properties with state, but they can have abstract properties that implementers must provide.

#### 5. Creating implementations of the sealed interface

Now I'll create concrete implementations of our sealed interface.

I'll create an object (singleton) for the Play action since we don't need multiple instances with different state.

```kotlin
object Play : MediaAction {
    override val actionName: String = "Play"
    override val requiresNetwork: Boolean = false
    
    fun execute(content: MediaContent) {
        println("Playing ${content.title} (${content.formattedDuration()})")
    }
}
```

Next, let's create a Download action.

For the Download action, I'll use a data class since we need to track the quality setting.

```kotlin
data class Download(val quality: String) : MediaAction {
    override val actionName: String = "Download"
    override val requiresNetwork: Boolean = true
    
    fun execute(content: MediaContent) {
        println("Downloading ${content.title} at $quality quality")
    }
}
```

Let's add one more action type - Share.

For the Share action, I'll use a data class to capture who we're sharing with.

```kotlin
data class Share(val platform: String) : MediaAction {
    override val actionName: String = "Share"
    override val requiresNetwork: Boolean = true
    
    fun execute(content: MediaContent) {
        println("Sharing ${content.title} on $platform")
    }
}
```

#### 6. Creating a nested sealed hierarchy for playback states

Now, let's demonstrate a more complex example - a nested sealed hierarchy for representing playback states.

I'll start with a base sealed class for PlaybackState.

```kotlin
sealed class PlaybackState {
```

First, I'll create a Playing state that needs to track the current position.

```kotlin
    data class Playing(
        val content: MediaContent,
        val currentPositionSeconds: Int
    ) : PlaybackState() {
        fun remainingTime(): Int = content.durationSeconds - currentPositionSeconds
    }
```

Next, I'll create a Paused state, which is similar but indicates playback is paused.

```kotlin
    data class Paused(
        val content: MediaContent,
        val currentPositionSeconds: Int
    ) : PlaybackState() {
        fun percentComplete(): Double = 
            (currentPositionSeconds.toDouble() / content.durationSeconds) * 100
    }
```

Now I'll add a Stopped state, which doesn't need position information.

```kotlin
    object Stopped : PlaybackState()
```

Finally, let's add a Loading state to indicate when content is being buffered.

```kotlin
    data class Loading(
        val content: MediaContent,
        val loadingProgress: Int // 0-100
    ) : PlaybackState()
}
```

This nested approach keeps related states grouped together, making the code more organized and maintainable.

#### 7. Creating a function to process playback states

Now let's create a function that processes our playback states and demonstrates exhaustive handling.

This function will take a PlaybackState and generate appropriate UI text.

```kotlin
fun getPlaybackStatusText(state: PlaybackState): String {
```

I'll use a when expression to handle each possible state.

```kotlin
    return when (state) {
```

For each specific state, I can safely access its unique properties.

```kotlin
        is PlaybackState.Playing -> {
            val remaining = state.remainingTime()
            "Playing: ${state.content.title} (${remaining / 60}:${remaining % 60} remaining)"
        }
        is PlaybackState.Paused -> {
            val percent = String.format("%.1f", state.percentComplete())
            "Paused: ${state.content.title} ($percent% complete)"
        }
        is PlaybackState.Stopped -> "Stopped"
        is PlaybackState.Loading -> "Loading ${state.content.title}: ${state.loadingProgress}%"
    }
}
```

Again, no else branch is needed because the compiler knows all possible states.

#### 8. Demonstrating everything in the `main` function

Let's put everything together in a main function to see how it works in practice.

```kotlin
fun main() {
```

First, I'll create instances of our different media content types.

```kotlin
    // Create media content instances
    val song = Song(
        title = "Believer",
        durationSeconds = 204,
        artist = "Imagine Dragons",
        album = "Evolve",
        genre = "Pop Rock"
    )
    
    val podcast = Podcast(
        title = "Modern Software Development",
        durationSeconds = 1800,
        host = "Jane Smith",
        episode = 42,
        showName = "Tech Talk Weekly"
    )
    
    val audiobook = Audiobook(
        title = "The Great Gatsby",
        durationSeconds = 9000,
        author = "F. Scott Fitzgerald",
        narrator = "Jake Gyllenhaal",
        chapters = listOf("Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4")
    )
```

Now, let's demonstrate getting descriptions for each media type.

```kotlin
    // Test the description function
    println("Media Descriptions:")
    println(getMediaDescription(song))
    println(getMediaDescription(podcast))
    println(getMediaDescription(audiobook))
    println()
```

Next, let's test our media actions.

```kotlin
    // Test media actions
    println("Media Actions:")
    Play.execute(song)
    Download("High").execute(podcast)
    Share("Twitter").execute(audiobook)
    println()
```

Finally, let's test our playback states.

```kotlin
    // Test playback states
    println("Playback States:")
    val playingState = PlaybackState.Playing(song, 45)
    val pausedState = PlaybackState.Paused(podcast, 900)
    val stoppedState = PlaybackState.Stopped
    val loadingState = PlaybackState.Loading(audiobook, 75)
    
    println(getPlaybackStatusText(playingState))
    println(getPlaybackStatusText(pausedState))
    println(getPlaybackStatusText(stoppedState))
    println(getPlaybackStatusText(loadingState))
}
```

When we run this code, we'll see how sealed classes and interfaces allow us to define restricted hierarchies where the compiler ensures exhaustive handling of all possible cases. This makes our code safer and more maintainable, especially as the system evolves over time.

### Best practices and pitfalls

Let me share some tips from experience:

- **Keep hierarchies focused:**
    - Sealed hierarchies should represent a clear domain concept with a finite set of variations. If you find yourself constantly adding new subclasses, it might indicate that your hierarchy is too broad and should be reconsidered.
- **Consider nesting vs. same-file organization:**
    - Nested sealed hierarchies (subclasses defined inside the sealed class) keep everything in one place, which can improve readability for smaller hierarchies. For larger hierarchies, using same-file organization (subclasses defined outside but in the same file) can make each subclass definition clearer.
- **Leverage data classes for value-based subclasses:**
    - When subclasses primarily represent data, use data classes to automatically get `equals()`, `hashCode()`, `toString()`, and `copy()` implementations.
- **Use objects for singleton subclasses:**
    - For subclasses that don't need state or represent singular concepts, use objects to ensure only one instance ever exists.
- **Watch out for sealed interface implementations outside the module:**
    - While sealed classes restrict subclasses to the same file or nested, sealed interfaces allow implementations in the same module. Be aware of this when checking for exhaustiveness, especially in larger projects.
- **Combine with other Kotlin features:**
    - Sealed hierarchies work well with pattern matching (`when`), smart casting, and extension functions to create expressive and type-safe code.
- **Avoid too many subclasses:**
    - If your sealed hierarchy has many subclasses (e.g., more than 7-10), it might be a sign that you need to refactor or use a different design. Consider grouping related subclasses into nested sealed hierarchies.
- **Be cautious with deep hierarchies:**
    - While sealed hierarchies can be nested (sealed classes inside sealed classes), too many levels can make the code hard to follow. Try to keep the hierarchy relatively flat when possible.
- **Remember serialization considerations:**
    - If you need to serialize sealed hierarchies (e.g., for JSON), you may need additional configuration in libraries like Jackson or Kotlin Serialization to properly handle the hierarchy.

### Conclusion

Sealed classes and interfaces represent one of Kotlin's most elegant features for building safer, more expressive code. By creating restricted hierarchies where all possible subtypes are known to the compiler, they enable exhaustive checking that eliminates entire categories of potential bugs.

In our media streaming application example, we've seen how sealed hierarchies let us model different media types, actions, and playback states in a way that's both flexible and type-safe. The compiler ensures we handle all cases in our `when` expressions, making our code more robust as the system evolves.

As you continue working with Kotlin, you'll find sealed hierarchies essential for representing domain concepts like states, events, results, and other scenarios where you need to model a finite set of variations. They combine the expressiveness of inheritance with the safety of enumerations, giving you powerful tools for creating maintainable and correct code.

Remember that sealed hierarchies shine brightest when used appropriately - for representing concepts with a clear, finite set of variations, where exhaustive handling is important, and where each variant may need its own properties and behaviors. Used in these contexts, sealed classes and interfaces will become an indispensable part of your Kotlin programming toolkit.