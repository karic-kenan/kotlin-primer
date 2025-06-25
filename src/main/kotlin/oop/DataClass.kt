package oop

import kotlin.collections.find
import kotlin.collections.forEach
import kotlin.collections.groupBy
import kotlin.collections.sortedByDescending

// Data class representing a Movie in the database
data class Movie(
    val title: String,
    val director: String,
    val releaseYear: Int,
    val genre: String,
    val rating: Double
) {
    // Custom method to display movie info
    fun displayInfo(): String {
        return "Title: $title, Director: $director, Year: $releaseYear, Genre: $genre, Rating: $rating"
    }
}

fun main() {
    // Creating instances of the Movie data class
    val movie1 = Movie("Inception", "Christopher Nolan", 2010, "Science Fiction", 8.8)
    val movie2 = Movie("The Dark Knight", "Christopher Nolan", 2008, "Action", 9.0)
    val movie3 = Movie("Inception", "Christopher Nolan", 2010, "Science Fiction", 8.8)

    // Demonstrating the automatically generated toString() method
    println("Movie 1: $movie1")
    println("Movie 2: $movie2")

    // Demonstrating the automatically generated equals() and hashCode() methods
    println("Movie 1 equals Movie 3: ${movie1 == movie3}") // True
    println("Movie 1 hash code: ${movie1.hashCode()}")
    println("Movie 3 hash code: ${movie3.hashCode()}")

    // Using the copy() method to create a new instance with some modifications
    val updatedMovie1 = movie1.copy(rating = 9.0)
    println("Updated Movie 1: $updatedMovie1")

    // Using componentN() functions to destructure the data class
    val (title, director, releaseYear, genre, rating) = movie2
    println("Destructured Movie 2: Title - $title, Director - $director, Year - $releaseYear, Genre - $genre, Rating - $rating")

    // Using data classes in a collection
    val movieDatabase = mutableListOf(movie1, movie2)
    println("Movie Database contains:")
    for (movie in movieDatabase) {
        println(movie)
    }

    // Finding a movie in the database based on title
    val searchTitle = "Inception"
    val foundMovie = movieDatabase.find { it.title == searchTitle }
    println("Movie found: $foundMovie")

    // Updating a movie rating in the database (immutable update using copy)
    val newRating = 9.1
    val movieToUpdate = movieDatabase.find { it.title == "Inception" }
    if (movieToUpdate != null) {
        val updatedMovie = movieToUpdate.copy(rating = newRating)
        movieDatabase[movieDatabase.indexOf(movieToUpdate)] = updatedMovie
    }

    // Displaying the updated movie database
    println("Updated Movie Database:")
    movieDatabase.forEach { println(it) }

    // Demonstrating usage of data classes in sorting
    val sortedMovies = movieDatabase.sortedByDescending { it.rating }
    println("Movies sorted by rating:")
    sortedMovies.forEach { println(it) }

    // Example of grouping movies by genre
    val moviesByGenre = movieDatabase.groupBy { it.genre }
    println("Movies grouped by genre:")
    moviesByGenre.forEach { (genre, movies) ->
        println("$genre:")
        movies.forEach { println(it) }
    }
}
