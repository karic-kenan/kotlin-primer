package misc

import java.time.LocalTime
import kotlin.collections.filterIsInstance
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.mapNotNull

object Casting {
    // Base class for all media items
    abstract class MediaItem(
        open val title: String,
        open val releaseYear: Int
    ) {
        fun displayBasicInfo() {
            println("Title: $title, Released: $releaseYear")
        }

        abstract fun play()
    }

    // Movie class extending MediaItem
    class Movie(
        override val title: String,
        override val releaseYear: Int,
        val director: String,
        val duration: Int  // duration in minutes
    ) : MediaItem(title, releaseYear) {
        override fun play() {
            println("Playing movie: $title directed by $director")
        }

        fun showDetails() {
            println("Movie: $title ($releaseYear)")
            println("Director: $director")
            println("Duration: $duration minutes")
        }
    }

    // Book class extending MediaItem
    class Book(
        override val title: String,
        override val releaseYear: Int,
        val author: String,
        val pages: Int
    ) : MediaItem(title, releaseYear) {
        override fun play() {
            println("Opening book: $title by $author")
        }

        fun showBookInfo() {
            println("Book: $title ($releaseYear)")
            println("Author: $author")
            println("Pages: $pages")
        }
    }

    // Music class extending MediaItem
    class Music(
        override val title: String,
        override val releaseYear: Int,
        val artist: String,
        val durationSeconds: Int
    ) : MediaItem(title, releaseYear) {
        override fun play() {
            println("Playing music: $title by $artist")
        }

        fun showMusicInfo() {
            println("Music: $title ($releaseYear)")
            println("Artist: $artist")
            println("Duration: ${durationSeconds / 60}:${durationSeconds % 60}")
        }
    }

    // Implicit casting (upcasting) examples
    fun demonstrateImplicitCasting() {
        val movie = Movie("Inception", 2010, "Christopher Nolan", 148)
        val book = Book("The Hobbit", 1937, "J.R.R. Tolkien", 310)
        val music = Music("Bohemian Rhapsody", 1975, "Queen", 354)

        // Implicit upcasting happens automatically
        val mediaItem1: MediaItem = movie  // Movie to MediaItem
        val mediaItem2: MediaItem = book   // Book to MediaItem
        val mediaItem3: MediaItem = music  // Music to MediaItem

        // We can call methods from MediaItem on all of them
        mediaItem1.displayBasicInfo()
        mediaItem2.displayBasicInfo()
        mediaItem3.displayBasicInfo()

        // The play method is polymorphic
        mediaItem1.play()  // Calls Movie's implementation
        mediaItem2.play()  // Calls Book's implementation
        mediaItem3.play()  // Calls Music's implementation

        // This would not compile:
        // mediaItem1.showDetails()  // Error: showDetails is not a member of MediaItem

        println("Implicit casting demonstration complete")
    }

    // Type checking and smart casting examples
    fun demonstrateTypeCheckingAndSmartCasting(mediaItem: MediaItem) {
        println("\nType checking for: ${mediaItem.title}")

        // Type checking with 'is' operator
        if (mediaItem is Movie) {
            // Smart casting in action - mediaItem is treated as Movie here
            println("This is a movie directed by ${mediaItem.director}")
            mediaItem.showDetails()  // Can call Movie-specific methods
        } else if (mediaItem is Book) {
            // Smart casting again - mediaItem is treated as Book here
            println("This is a book written by ${mediaItem.author}")
            mediaItem.showBookInfo()  // Can call Book-specific methods
        } else if (mediaItem is Music) {
            // Smart casting again - mediaItem is treated as Music here
            println("This is a music track by ${mediaItem.artist}")
            mediaItem.showMusicInfo()  // Can call Music-specific methods
        }

        // Using 'when' with type checking
        when (mediaItem) {
            is Movie -> println("Movie duration: ${mediaItem.duration} minutes")
            is Book -> println("Book length: ${mediaItem.pages} pages")
            is Music -> println("Music duration: ${mediaItem.durationSeconds / 60} minutes ${mediaItem.durationSeconds % 60} seconds")
            else -> println("Unknown media type")
        }
    }

    // Explicit casting examples
    fun demonstrateExplicitCasting() {
        println("\nExplicit casting demonstration:")

        val mediaList = listOf(
            Movie("The Matrix", 1999, "Wachowski Brothers", 136),
            Book("1984", 1949, "George Orwell", 328),
            Music("Stairway to Heaven", 1971, "Led Zeppelin", 482)
        )

        // Unsafe casting with 'as'
        try {
            val firstItem = mediaList[0]
            val movie = firstItem as Movie  // This will succeed
            println("Successfully cast to Movie: ${movie.title}, directed by ${movie.director}")

            val secondItem = mediaList[1]
            val anotherMovie = secondItem as Movie  // This will fail with ClassCastException
            println("This line won't be executed")
        } catch (e: ClassCastException) {
            println("Cast failed with exception: ${e.message}")
        }

        // Safe casting with 'as?'
        val thirdItem = mediaList[2]
        val music = thirdItem as? Music  // This will succeed

        if (music != null) {
            println("Successfully cast to Music: ${music.title} by ${music.artist}")
        }

        val book = thirdItem as? Book  // This will return null, not throw an exception
        if (book != null) {
            println("Cast to Book succeeded")
        } else {
            println("Cast to Book returned null (cast failed safely)")
        }

        // Often used with Elvis operator for default values
        val pages = (thirdItem as? Book)?.pages ?: 0
        println("Pages (defaults to 0 if not a Book): $pages")
    }

    // Working with collections and casting
    fun demonstrateCollectionCasting() {
        println("\nCollection casting demonstration:")

        val mixedMediaLibrary = listOf(
            Movie("Pulp Fiction", 1994, "Quentin Tarantino", 154),
            Book("To Kill a Mockingbird", 1960, "Harper Lee", 281),
            Music("Imagine", 1971, "John Lennon", 183),
            Movie("The Godfather", 1972, "Francis Ford Coppola", 175),
            Book("Pride and Prejudice", 1813, "Jane Austen", 432),
            Music("Thriller", 1982, "Michael Jackson", 358)
        )

        // Filtering a collection by type
        val movies = mixedMediaLibrary.filterIsInstance<Movie>()
        println("Found ${movies.size} movies:")

        movies.forEach { movie ->
            println("- ${movie.title} (${movie.releaseYear}) directed by ${movie.director}")
        }

        // Processing items based on their type
        println("\nProcessing each item based on its type:")

        mixedMediaLibrary.forEach { item ->
            when (item) {
                is Movie -> println("Movie: ${item.title} (${item.duration} mins)")
                is Book -> println("Book: ${item.title} (${item.pages} pages)")
                is Music -> println("Music: ${item.title} (${item.durationSeconds / 60}:${item.durationSeconds % 60})")
            }
        }

        // Extracting properties from different types
        val titles = mixedMediaLibrary.map { it.title }
        println("\nAll titles: $titles")

        val durations = mixedMediaLibrary.mapNotNull { item ->
            when (item) {
                is Movie -> item.duration
                is Music -> item.durationSeconds / 60
                else -> null
            }
        }
        println("Durations in minutes: $durations")
    }


    @JvmStatic
    fun main(args: Array<String>) {

        // Create media items
        val movie = Movie("Interstellar", 2014, "Christopher Nolan", 169)
        val book = Book("Dune", 1965, "Frank Herbert", 412)
        val music = Music("Hotel California", 1976, "Eagles", 390)

        // Demonstrate different casting techniques
        demonstrateImplicitCasting()

        println("\nTesting different media types with type checking:")
        demonstrateTypeCheckingAndSmartCasting(movie)
        demonstrateTypeCheckingAndSmartCasting(book)
        demonstrateTypeCheckingAndSmartCasting(music)

        demonstrateExplicitCasting()

        demonstrateCollectionCasting()

        // Real-world scenario: A media player function that handles any media type
        println("\nMedia Player Demonstration:")

        fun playMedia(media: MediaItem) {
            println("Now playing: ${media.title} (${media.releaseYear})")
            media.play()

            // Extra operations based on media type
            when (media) {
                is Movie -> println(
                    "Movie will end at ${
                        LocalTime.now().plusMinutes(media.duration.toLong())
                    }"
                )

                is Book -> println("Estimated reading time: ${media.pages / 20} minutes")
                is Music -> println("${media.durationSeconds / 60}:${media.durationSeconds % 60} remaining")
            }
            println("-------------------------------")
        }

        // Play different media types
        playMedia(movie)
        playMedia(book)
        playMedia(music)
    }
}
