package functions

import kotlin.collections.drop
import kotlin.collections.find
import kotlin.collections.first
import kotlin.collections.plus

object TailRecursion {

    data class File(val name: String)

    data class Directory(
        val name: String,
        val files: List<File> = listOf(),
        val subdirectories: List<Directory> = listOf()
    )

    // Non-tail-recursive version for comparison
    fun searchFileNonTail(
        directory: Directory,
        fileName: String
    ): File? {
        // First check files in current directory
        val foundFile = directory.files.find { it.name == fileName }
        if (foundFile != null) return foundFile

        // Then recursively search subdirectories
        for (subdir in directory.subdirectories) {
            val result = searchFileNonTail(subdir, fileName)
            if (result != null) return result
        }

        return null
    }

    // Tail-recursive version using breadth-first search
    tailrec fun searchFileTailRec(
        fileName: String,
        queue: List<Directory>  // Moving parameters helps emphasize state transformation
    ): File? {
        // Base case: no more directories to search
        if (queue.isEmpty()) return null

        val currentDir = queue.first()

        // Check if file exists in current directory
        val foundFile = currentDir.files.find { it.name == fileName }
        if (foundFile != null) return foundFile

        // Process next directories (rest of queue + subdirectories of current)
        val newQueue = queue.drop(1) + currentDir.subdirectories

        // Tail-recursive call as the final operation
        return searchFileTailRec(fileName, newQueue)
    }

    // Helper function with default parameters for easier API
    fun searchFile(directory: Directory, fileName: String): File? {
        return searchFileTailRec(fileName, listOf(directory))
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // Create a sample directory structure for testing
        val deepDir = Directory(
            name = "deepDir",
            files = listOf(File("target.txt"))
        )

        val subSubDir1 = Directory(
            name = "subSubDir1",
            files = listOf(File("file5.txt"), File("file6.txt")),
            subdirectories = listOf(deepDir)
        )

        val subDir1 = Directory(
            name = "subDir1",
            files = listOf(File("file3.txt")),
            subdirectories = listOf(subSubDir1)
        )

        val subDir2 = Directory(
            name = "subDir2",
            files = listOf(File("file4.txt"))
        )

        val rootDir = Directory(
            name = "rootDir",
            files = listOf(File("file1.txt"), File("file2.txt")),
            subdirectories = listOf(subDir1, subDir2)
        )

        // Test both implementations
        val fileNameToSearch = "target.txt"

        println("Testing non-tail-recursive implementation:")
        val startTime1 = System.nanoTime()
        val foundFile1 = searchFileNonTail(rootDir, fileNameToSearch)
        val endTime1 = System.nanoTime()
        println("Result: ${foundFile1?.name ?: "File not found"}")
        println("Time taken: ${(endTime1 - startTime1) / 1_000_000.0} ms")

        println("\nTesting tail-recursive implementation:")
        val startTime2 = System.nanoTime()
        val foundFile2 = searchFile(rootDir, fileNameToSearch)
        val endTime2 = System.nanoTime()
        println("Result: ${foundFile2?.name ?: "File not found"}")
        println("Time taken: ${(endTime2 - startTime2) / 1_000_000.0} ms")

        // Demonstrate stack safety with a very deep directory structure
        println("\nCreating a deeply nested directory structure to demonstrate stack safety...")
        var deepNestDir = Directory(name = "level_0", files = listOf())

        // Create a directory structure that would cause StackOverflowError with non-tail recursion
        for (i in 1..10000) {
            deepNestDir = Directory(
                name = "level_$i",
                subdirectories = listOf(deepNestDir)
            )

            // Add the target file at a deep level
            if (i == 9999) {
                deepNestDir = Directory(
                    name = "level_$i",
                    files = listOf(File("deep_target.txt")),
                    subdirectories = listOf(deepNestDir)
                )
            }
        }

        println("Searching for deep_target.txt using tail recursion...")
        val startTime3 = System.nanoTime()
        val foundDeepFile = searchFile(deepNestDir, "deep_target.txt")
        val endTime3 = System.nanoTime()
        println("Result: ${foundDeepFile?.name ?: "File not found"}")
        println("Time taken: ${(endTime3 - startTime3) / 1_000_000.0} ms")

        println("\nNote: The non-tail-recursive version would throw a StackOverflowError on the deep structure")
        // Uncomment to demonstrate stack overflow (caution: will crash!)
        // searchFileNonTail(deepNestDir, "deep_target.txt")
    }
}
