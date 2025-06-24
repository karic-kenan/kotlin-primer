package oop

object Classes {
    // Class representing a Book in the library
    class Book(
        val title: String,
        private val author: String,
        var pages: Int,
        private var isAvailable: Boolean = true // Private field, mutable
    ) {
        // Public method to borrow the book
        fun borrow(): Boolean {
            return if (isAvailable) {
                isAvailable = false
                println("Book '$title' has been borrowed")
                true
            } else {
                println("Sorry, '$title' is not available")
                false
            }
        }

        // Public method to return the book
        fun returnBook() {
            isAvailable = true
            println("The book '$title' has been returned.")
        }

        // Public method to get book details
        fun getDetails(): String {
            val status = if (isAvailable) "Available" else "Borrowed"
            return "Title: $title, Author: $author, Pages: $pages, Status: $status"
        }
    }

    // Class representing a Member of the library
    class Member(
        var firstName: String,
        var lastName: String,
        private val memberId: String,
        private val maxBooksAllowed: Int = 5 // Private constant-like property
    ) {
        // Backing field for fullName
        var fullName: String
            get() = "$firstName $lastName"
            private set(value) {
                val names = value.split(" ")
                if (names.size == 2) {
                    firstName = names[0]
                    lastName = names[1]
                }
            }

        // lateinit property for address (initialized later)
        lateinit var address: String

        // List of borrowed books
        private val borrowedBooks = mutableListOf<Book>()

        // Secondary constructor to allow setting address during member creation
        constructor(firstName: String, lastName: String, memberId: String, address: String) : this(
            firstName,
            lastName,
            memberId
        ) {
            this.address = address
        }

        // Init block for additional setup
        init {
            println("New member created: $fullName with Member ID: $memberId")
        }

        // Public method to borrow a book
        fun borrowBook(book: Book) {
            if (borrowedBooks.size >= maxBooksAllowed) {
                println("$fullName cannot borrow more than $maxBooksAllowed books.")
                return
            }
            if (book.borrow()) {
                borrowedBooks.add(book)
                println("$fullName has borrowed '${book.title}'.")
            }
        }

        // Public method to return a book
        fun returnBook(book: Book) {
            if (borrowedBooks.remove(book)) {
                book.returnBook()
                println("$fullName has returned '${book.title}'.")
            } else {
                println("$fullName does not have '${book.title}'.")
            }
        }

        // Public method to update address
        fun updateAddress(address: String) {
            if (!this::address.isInitialized) {
                this.address = address
            }
            println("Address updated for $fullName: $address")
        }

        // Public method to get member details
        fun getMemberDetails(): String {
            return "Member ID: $memberId, Name: $fullName, Address: $address, Borrowed Books: ${borrowedBooks.size}"
        }
    }

    // Class representing the Library
    class Library(
        private val name: String,
        private val books: MutableList<Book> = mutableListOf()
    ) {
        // Method to add a book to the library
        fun addBook(book: Book) {
            books.add(book)
            println("Book '${book.title}' added to the library.")
        }

        // Method to list all books
        fun listBooks() {
            println("Books available in $name:")
            books.forEach { println(it.getDetails()) }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // Create a library
        val library = Library("Central Library")

        // Add books to the library
        val book1 = Book("The Great Gatsby", "F. Scott Fitzgerald", 180)
        val book2 = Book("1984", "George Orwell", 328)
        library.addBook(book1)
        library.addBook(book2)

        // List all books
        library.listBooks()

        // Create a member
        val member = Member("John", "Doe", "M001", "123 Maple Street")
        println("Full name: ${member.fullName}")
        member.borrowBook(book1)
        member.borrowBook(book2)

        // List all books again to see availability
        library.listBooks()

        // Return a book
        member.returnBook(book1)

        // Update member address
        member.updateAddress("456 Oak Avenue")

        // Get member details
        println(member.getMemberDetails())
    }
}
