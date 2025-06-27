package collections

import kotlin.collections.first
import kotlin.collections.intersect
import kotlin.collections.map
import kotlin.collections.mapNotNull
import kotlin.collections.minus
import kotlin.collections.set
import kotlin.collections.toSet
import kotlin.collections.union
import kotlin.ranges.until

// User class with proper equals and hashCode implementation for Set operations
data class User(val id: Long, val name: String) {
    // Two users are the same if they have the same id
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

// Post class that uses Sets to track user interactions
class Post(val id: Long, val author: User, val content: String) {
    // Use Sets to track unique users who've interacted with the post
    private val _likes = mutableSetOf<User>()
    private val _shares = mutableSetOf<User>()

    // We'll use read-only views for public access
    val likes: Set<User> get() = _likes
    val shares: Set<User> get() = _shares

    // Track comments with user info
    private val _comments = mutableListOf<Comment>()
    val comments: List<Comment> get() = _comments

    // Nested class for comments
    data class Comment(val user: User, val text: String)

    // Method for a user to like a post
    fun like(user: User): Boolean {
        // The add method returns true only if the element was actually added
        return _likes.add(user)
    }

    // Method to unlike a post
    fun unlike(user: User): Boolean {
        return _likes.remove(user)
    }

    // Method to add a comment
    fun addComment(user: User, commentText: String) {
        _comments.add(Comment(user, commentText))
    }

    // Methods for sharing
    fun share(user: User): Boolean {
        return _shares.add(user)
    }

    fun unshare(user: User): Boolean {
        return _shares.remove(user)
    }

    // Find users who both liked and shared the post
    fun getUsersWhoLikedAndShared(): Set<User> {
        return _likes.intersect(_shares)
    }

    // Find all users who engaged with the post in any way
    fun getAllEngagedUsers(): Set<User> {
        // Start with users who liked or shared
        val engagedUsers = _likes.union(_shares)

        // Add users who commented
        val commenters = _comments.map { it.user }.toSet()
        return engagedUsers.union(commenters)
    }

    // Find users who liked but didn't share
    fun getUsersWhoLikedButDidntShare(): Set<User> {
        return _likes.minus(_shares)
    }
}

// SocialNetwork class to manage posts and users
class SocialNetwork {
    private val posts = mutableMapOf<Long, Post>()
    private val users = mutableSetOf<User>()

    // Add a user to the network
    fun addUser(user: User) {
        users.add(user)
    }

    // Get all users
    fun getAllUsers(): Set<User> = users.toSet() // Return a copy to prevent modification

    // Add a post
    fun addPost(post: Post) {
        posts[post.id] = post
    }

    // Get a post by id
    fun getPost(postId: Long): Post? = posts[postId]

    // Find users who have engaged with any post
    fun getActiveUsers(): Set<User> {
        val activeUsers = mutableSetOf<User>()

        for (post in posts.values) {
            // Union the current set with all engaged users from this post
            activeUsers.addAll(post.getAllEngagedUsers())
        }

        return activeUsers
    }

    // Find users who engaged with all of the specified posts
    fun getUsersEngagedWithAllPosts(postIds: Set<Long>): Set<User> {
        // We'll use the fact that intersection of an empty set with anything is empty
        if (postIds.isEmpty()) return emptySet()

        // First, get the posts by id, filtering out any invalid ids
        val postsToCheck = postIds.mapNotNull { posts[it] }
        if (postsToCheck.isEmpty()) return emptySet()

        // Start with all users from the first post
        var result = postsToCheck.first().getAllEngagedUsers()

        // Intersect with each subsequent post
        for (i in 1 until postsToCheck.size) {
            result = result.intersect(postsToCheck[i].getAllEngagedUsers())
        }

        return result
    }
}

// Main function to demonstrate everything
fun main() {
    // Create some users
    val alice = User(1, "Alice")
    val bob = User(2, "Bob")
    val charlie = User(3, "Charlie")
    val dave = User(4, "Dave")

    // Create the social network and add users
    val socialNetwork = SocialNetwork()
    socialNetwork.addUser(alice)
    socialNetwork.addUser(bob)
    socialNetwork.addUser(charlie)
    socialNetwork.addUser(dave)

    // Create posts
    val post1 = Post(101, alice, "Set theory is fascinating!")
    val post2 = Post(102, bob, "Kotlin collections are powerful!")

    // Add posts to the network
    socialNetwork.addPost(post1)
    socialNetwork.addPost(post2)

    // Users interact with post1
    post1.like(bob)
    post1.like(charlie)
    post1.like(dave)
    post1.share(charlie)
    post1.share(dave)
    post1.addComment(bob, "I totally agree!")
    post1.addComment(charlie, "I've been studying this.")

    // Users interact with post2
    post2.like(alice)
    post2.like(charlie)
    post2.share(dave)
    post2.addComment(alice, "Especially Sets!")
    post2.addComment(dave, "I'm learning a lot.")

    // Post-specific analytics
    println("Post 1 has ${post1.likes.size} likes")
    println("Users who liked and shared post 1: ${post1.getUsersWhoLikedAndShared().map { it.name }}")
    println("Users who liked but didn't share post 1: ${post1.getUsersWhoLikedButDidntShare().map { it.name }}")

    // Network-wide analytics
    println("Active users across the network: ${socialNetwork.getActiveUsers().map { it.name }}")

    // Find users engaged with both posts
    val bothPosts = setOf(post1.id, post2.id)
    val usersEngagedWithBoth = socialNetwork.getUsersEngagedWithAllPosts(bothPosts)
    println("Users engaged with both posts: ${usersEngagedWithBoth.map { it.name }}")
}
