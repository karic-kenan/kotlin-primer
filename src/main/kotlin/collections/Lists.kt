package collections

import kotlin.collections.all
import kotlin.collections.count
import kotlin.collections.filter
import kotlin.collections.find
import kotlin.collections.first
import kotlin.collections.flatMap
import kotlin.collections.flatten
import kotlin.collections.forEach
import kotlin.collections.getOrNull
import kotlin.collections.groupBy
import kotlin.collections.indexOfFirst
import kotlin.collections.last
import kotlin.collections.map
import kotlin.collections.sortedBy
import kotlin.collections.toList
import kotlin.to

// Task class representing a to-do item
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val priority: Int,
    var isCompleted: Boolean = false
)

// TaskManager class to demonstrate practical use of Lists
class TaskManager {
    private val tasks = mutableListOf<Task>()

    // Add a new task
    fun addTask(task: Task) {
        tasks.add(task)
    }

    // Get all tasks
    fun getAllTasks(): List<Task> {
        return tasks.toList() // Returns an immutable copy
    }

    // Get tasks by priority
    fun getTasksByPriority(priority: Int): List<Task> {
        return tasks.filter { it.priority == priority }
    }

    // Get completed tasks
    fun getCompletedTasks(): List<Task> {
        return tasks.filter { it.isCompleted }
    }

    // Find a task by ID
    fun findTaskById(id: Int): Task? {
        return tasks.find { it.id == id }
    }

    // Mark a task as completed
    fun completeTask(id: Int): Boolean {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            // We need to create a new Task since properties are immutable
            val updatedTask = tasks[index].copy(isCompleted = true)
            tasks[index] = updatedTask
            return true
        }
        return false
    }

    // Remove a task
    fun removeTask(id: Int): Boolean {
        return tasks.removeIf { it.id == id }
    }

    // Get tasks sorted by priority
    fun getTasksSortedByPriority(): List<Task> {
        return tasks.sortedBy { it.priority }
    }

    // Get task statistics
    fun getTaskStatistics(): Map<String, Int> {
        return mapOf(
            "total" to tasks.size,
            "completed" to tasks.count { it.isCompleted },
            "highPriority" to tasks.count { it.priority == 1 }
        )
    }
}

fun main() {
    // Creating sample tasks
    val task1 = Task(1, "Complete Kotlin assignment", "Finish collections lecture example", 1, false)
    val task2 = Task(2, "Buy groceries", "Get milk, eggs, and bread", 2, false)
    val task3 = Task(3, "Call mom", "Discuss weekend plans", 3, false)

    // Creating an immutable List
    val taskList = listOf(task1, task2, task3)

    // Printing the immutable List
    println("Tasks:")
    taskList.forEach { println(it) }

    // Creating a mutable List with the same elements
    val mutableTaskList = mutableListOf(task1, task2, task3)

    // Creating an empty mutable List
    val newTaskList = mutableListOf<Task>()

    // Accessing elements by index
    val firstTask = taskList[0]  // Using index operator
    println("\nFirst task: ${firstTask.title}")

    // Using get() function
    val secondTask = taskList.get(1)
    println("Second task: ${secondTask.title}")

    // Accessing properties from elements
    println("High priority tasks:")
    for (task in taskList) {
        if (task.priority == 1) {
            println("- ${task.title}")
        }
    }

    // Safely accessing elements with getOrNull()
    val fourthTask = taskList.getOrNull(3)
    println("\nFourth task: ${fourthTask?.title ?: "No fourth task exists"}")

    // Getting first and last elements
    println("First task: ${taskList.first().title}")
    println("Last task: ${taskList.last().title}")

    // Adding a new task
    val task4 = Task(4, "Do laundry", "Wash and fold clothes", 2, false)
    mutableTaskList.add(task4)

    // Adding at a specific position
    val task5 = Task(5, "Submit timesheet", "Complete before Friday", 1, false)
    mutableTaskList.add(1, task5)

    // Updating an element
    mutableTaskList[0] = Task(1, "Complete Kotlin project", "Finish collections lecture", 1, false)

    // Removing an element by object reference
    mutableTaskList.remove(task3)

    // Removing an element by index
    mutableTaskList.removeAt(mutableTaskList.size - 1)

    // Marking a task as completed
    if (mutableTaskList.size > 1) {
        val taskToUpdate = mutableTaskList[1]
        // Since our Task class properties are immutable (except isCompleted),
        // we need to create a new Task object with updated properties
        mutableTaskList[1] = taskToUpdate.copy(isCompleted = true)
    }

    // Printing the modified List
    println("\nModified task list:")
    mutableTaskList.forEach { println("${it.title} - Completed: ${it.isCompleted}") }

    // Filtering tasks
    val highPriorityTasks = taskList.filter { it.priority == 1 }
    println("\nHigh priority tasks: ${highPriorityTasks.map { it.title }}")

    // Mapping to transform elements
    val taskTitles = taskList.map { it.title }
    println("\nTask titles: $taskTitles")

    // Sorting tasks by priority
    val sortedByPriorityTasks = taskList.sortedBy { it.priority }
    println("\nTasks sorted by priority:")
    sortedByPriorityTasks.forEach { println("Priority ${it.priority}: ${it.title}") }

    // Finding elements
    val importantTask = taskList.find { it.priority == 1 }
    println("\nFound important task: ${importantTask?.title}")

    // Checking conditions
    val allHighPriority = taskList.all { it.priority <= 2 }
    println("\nAll tasks are high priority: $allHighPriority")

    // Grouping tasks by priority
    val groupedByPriority = taskList.groupBy { it.priority }
    println("\nTasks grouped by priority:")
    groupedByPriority.forEach { (priority, tasks) ->
        println("Priority $priority: ${tasks.map { it.title }}")
    }

    // Creating nested Lists for task categories
    val workTasks = listOf(
        Task(10, "Complete project", "Finish by deadline", 1, false),
        Task(11, "Email client", "Send weekly update", 2, false)
    )

    val personalTasks = listOf(
        Task(20, "Call dentist", "Schedule checkup", 2, false),
        Task(21, "Gym workout", "30 minutes cardio", 3, false)
    )

    // Creating a List of Lists
    val allTaskCategories = listOf(workTasks, personalTasks)

    // Accessing elements in nested Lists
    println("\nFirst work task: ${allTaskCategories[0][0].title}")

    // Flattening nested Lists
    val allTasksFlattened = allTaskCategories.flatten()
    println("\nAll tasks flattened:")
    allTasksFlattened.forEach { println(it.title) }

    // Using flatMap for transformation and flattening
    val allTaskIds = allTaskCategories.flatMap { category ->
        category.map { task -> task.id }
    }
    println("\nAll task IDs: $allTaskIds")

    // Creating a TaskManager instance
    val taskManager = TaskManager()

    // Adding tasks
    taskManager.addTask(Task(101, "Learn Kotlin", "Study collections", 1, false))
    taskManager.addTask(Task(102, "Exercise", "Go for a run", 2, false))
    taskManager.addTask(Task(103, "Read book", "Chapter 5", 3, false))

    // Completing a task
    taskManager.completeTask(102)

    // Getting all tasks
    println("\nAll tasks in TaskManager:")
    taskManager.getAllTasks().forEach { println("${it.title} - Completed: ${it.isCompleted}") }

    // Getting tasks by priority
    println("\nHigh priority tasks:")
    taskManager.getTasksByPriority(1).forEach { println(it.title) }

    // Getting completed tasks
    println("\nCompleted tasks:")
    taskManager.getCompletedTasks().forEach { println(it.title) }

    // Getting sorted tasks
    println("\nTasks sorted by priority:")
    taskManager.getTasksSortedByPriority().forEach { println("Priority ${it.priority}: ${it.title}") }

    // Getting task statistics
    val stats = taskManager.getTaskStatistics()
    println("\nTask statistics:")
    println("Total tasks: ${stats["total"]}")
    println("Completed tasks: ${stats["completed"]}")
    println("High priority tasks: ${stats["highPriority"]}")
}
