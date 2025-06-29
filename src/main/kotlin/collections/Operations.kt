package collections

import kotlin.collections.addAll
import kotlin.collections.any
import kotlin.collections.average
import kotlin.collections.count
import kotlin.collections.filter
import kotlin.collections.find
import kotlin.collections.flatMap
import kotlin.collections.forEach
import kotlin.collections.groupBy
import kotlin.collections.map
import kotlin.collections.maxByOrNull
import kotlin.collections.minByOrNull
import kotlin.collections.partition
import kotlin.collections.sortedBy
import kotlin.collections.sortedByDescending
import kotlin.collections.sumOf
import kotlin.collections.take
import kotlin.collections.toList
import kotlin.collections.toSet
import kotlin.text.split
import kotlin.to

object Operations {
    data class Book(
        val title: String,
        val author: String,
        val genre: String,
        val year: Int,
        val price: Double
    )

    // Library class to manage the book inventory
    class Library(private val books: MutableList<Book> = mutableListOf()) {
        // Method to add a book to the library
        fun addBook(book: Book) {
            books.add(book)
        }

        // Method to add multiple books at once
        fun addBooks(vararg books: Book) {
            this.books.addAll(books)
        }

        // Method to get all books
        fun getAllBooks(): List<Book> {
            return books.toList()
        }

        // Get all book titles using map
        fun getBookTitles(): List<String> {
            return books.map { it.title }
        }

        // Get all unique authors using map and toSet
        fun getUniqueAuthors(): Set<String> {
            return books.map { it.author }.toSet()
        }

        // Group books by genre using groupBy
        fun getBooksByGenre(): Map<String, List<Book>> {
            return books.groupBy { it.genre }
        }

        // Get all words used in book titles using flatMap
        fun getAllTitleWords(): List<String> {
            return books.map { it.title.split(" ") }.flatMap { it }
        }

        // Get books published after a certain year using filter
        fun getBooksPublishedAfter(year: Int): List<Book> {
            return books.filter { it.year > year }
        }

        // Get expensive books (price > threshold) using filter
        fun getExpensiveBooks(threshold: Double): List<Book> {
            return books.filter { it.price > threshold }
        }

        // Partition books into recent and older publications
        fun partitionBooksByRecency(recentYear: Int): Pair<List<Book>, List<Book>> {
            return books.partition { it.year >= recentYear }
        }

        // Count books by a specific author using count
        fun countBooksByAuthor(author: String): Int {
            return books.count { it.author == author }
        }

        // Calculate the total value of all books using sumOf
        fun calculateTotalValue(): Double {
            return books.sumOf { it.price }
        }

        // Find the most recent book using maxByOrNull
        fun getMostRecentBook(): Book? {
            return books.maxByOrNull { it.year }
        }

        // Find a book by title using find
        fun findBookByTitle(title: String): Book? {
            return books.find { it.title == title }
        }

        // Get the oldest book by a specific author using filter and minByOrNull
        fun getOldestBookByAuthor(author: String): Book? {
            return books.filter { it.author == author }.minByOrNull { it.year }
        }

        // Check if the library has any books in a specific genre using any
        fun hasGenre(genre: String): Boolean {
            return books.any { it.genre == genre }
        }

        // Get books sorted by publication year using sortedBy
        fun getBooksSortedByYear(): List<Book> {
            return books.sortedBy { it.year }
        }

        // Get books sorted by price in descending order using sortedByDescending
        fun getBooksSortedByPriceDescending(): List<Book> {
            return books.sortedByDescending { it.price }
        }

        // Get the top N most expensive books
        fun getTopExpensiveBooks(n: Int): List<Book> {
            return books.sortedByDescending { it.price }.take(n)
        }

        // Get average price of books by a specific author using filter and average
        fun getAveragePriceByAuthor(author: String): Double {
            return books.filter { it.author == author }
                .map { it.price }
                .average()
        }

        // Get the most prolific author (author with the most books)
        fun getMostProlificAuthor(): String? {
            return books.groupBy { it.author }
                .maxByOrNull { (_, books) -> books.size }
                ?.key
        }

        // Get genres sorted by their average book price
        fun getGenresByAveragePrice(): List<String> {
            return books.groupBy { it.genre }
                .map { (genre, booksInGenre) ->
                    genre to booksInGenre.map { it.price }.average()
                }
                .sortedByDescending { (_, averagePrice) -> averagePrice }
                .map { (genre, _) -> genre }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // Create a new library
        val library = Library()

        // Add sample books
        library.addBooks(
            Book("The Great Gatsby", "F. Scott Fitzgerald", "Classic", 1925, 12.99),
            Book("To Kill a Mockingbird", "Harper Lee", "Classic", 1960, 14.99),
            Book("1984", "George Orwell", "Science Fiction", 1949, 11.99),
            Book("Brave New World", "Aldous Huxley", "Science Fiction", 1932, 13.99),
            Book("The Hobbit", "J.R.R. Tolkien", "Fantasy", 1937, 10.99),
            Book("The Lord of the Rings", "J.R.R. Tolkien", "Fantasy", 1954, 24.99),
            Book("Harry Potter and the Philosopher's Stone", "J.K. Rowling", "Fantasy", 1997, 15.99),
            Book("The Catcher in the Rye", "J.D. Salinger", "Classic", 1951, 11.99),
            Book("Pride and Prejudice", "Jane Austen", "Classic", 1813, 9.99),
            Book("The Hunger Games", "Suzanne Collins", "Science Fiction", 2008, 16.99)
        )

        // Demonstrate various collection operations
        println("All book titles:")
        library.getBookTitles().forEach { println("  - $it") }

        println("\nUnique authors:")
        library.getUniqueAuthors().forEach { println("  - $it") }

        println("\nBooks grouped by genre:")
        library.getBooksByGenre().forEach { (genre, books) ->
            println("  $genre:")
            books.forEach { println("    - ${it.title} (${it.year})") }
        }

        println("\nBooks published after 1950:")
        library.getBooksPublishedAfter(1950).forEach {
            println("  - ${it.title} (${it.year})")
        }

        println("\nExpensive books (price > $15):")
        library.getExpensiveBooks(15.0).forEach {
            println("  - ${it.title}: $${it.price}")
        }

        println("\nPartitioning books by recency (2000 or later):")
        val (recent, older) = library.partitionBooksByRecency(2000)
        println("  Recent books:")
        recent.forEach { println("    - ${it.title} (${it.year})") }
        println("  Older books:")
        older.forEach { println("    - ${it.title} (${it.year})") }

        println("\nNumber of books by J.R.R. Tolkien: ${library.countBooksByAuthor("J.R.R. Tolkien")}")
        println("Total value of all books: $${library.calculateTotalValue()}")

        val mostRecent = library.getMostRecentBook()
        println("Most recent book: ${mostRecent?.title} (${mostRecent?.year})")

        val bookTitle = "1984"
        val foundBook = library.findBookByTitle(bookTitle)
        println("\nFound book '$bookTitle': ${foundBook != null}")

        val oldestByTolkien = library.getOldestBookByAuthor("J.R.R. Tolkien")
        println("Oldest book by J.R.R. Tolkien: ${oldestByTolkien?.title} (${oldestByTolkien?.year})")

        println("Library has Fantasy books: ${library.hasGenre("Fantasy")}")
        println("Library has Horror books: ${library.hasGenre("Horror")}")

        println("\nBooks sorted by year:")
        library.getBooksSortedByYear().forEach {
            println("  - ${it.title} (${it.year})")
        }

        println("\nTop 3 most expensive books:")
        library.getTopExpensiveBooks(3).forEach {
            println("  - ${it.title}: $${it.price}")
        }

        println("\nAverage price of books by J.R.R. Tolkien: $${library.getAveragePriceByAuthor("J.R.R. Tolkien")}")

        println("Most prolific author: ${library.getMostProlificAuthor()}")

        println("\nGenres sorted by average book price:")
        library.getGenresByAveragePrice().forEach { println("  - $it") }
    }
}