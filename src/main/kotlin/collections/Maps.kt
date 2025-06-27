package collections

import kotlin.collections.contains
import kotlin.collections.eachCount
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.getValue
import kotlin.collections.groupBy
import kotlin.collections.groupingBy
import kotlin.collections.isNotEmpty
import kotlin.collections.maxOrNull
import kotlin.collections.minOrNull
import kotlin.collections.set
import kotlin.collections.sortedBy
import kotlin.collections.toList
import kotlin.collections.toMap
import kotlin.collections.toSortedMap
import kotlin.text.contains
import kotlin.text.first
import kotlin.text.isBlank
import kotlin.text.matches
import kotlin.text.startsWith
import kotlin.text.substringBefore
import kotlin.text.uppercaseChar
import kotlin.to

class ContactManager {
    // Map to store names (keys) and phone numbers (values)
    private val contacts = mutableMapOf<String, String>()

    // Map to store contact groups
    private val contactGroups = mutableMapOf<String, MutableMap<String, String>>()

    // Add a new contact or update existing one
    fun addContact(name: String, phoneNumber: String) {
        if (name.isBlank()) throw kotlin.IllegalArgumentException("Contact name cannot be empty")
        if (!phoneNumber.matches(Regex("\\d{3}-\\d{4}"))) {
            throw kotlin.IllegalArgumentException("Phone number must be in format 555-1234")
        }

        contacts[name] = phoneNumber
        println("Contact $name added with number $phoneNumber")
    }

    // Get a contact's phone number
    fun getPhoneNumber(name: String): String? {
        return contacts[name]
    }

    // Get a phone number with a custom message if not found
    fun getPhoneNumberOrMessage(name: String): String {
        return contacts.getOrDefault(name, "Contact not found")
    }

    // Get a phone number or throw an exception if not found
    fun getPhoneNumberOrThrow(name: String): String {
        return contacts.getValue(name) // Throws NoSuchElementException if name not found
    }

    // Add multiple contacts at once
    fun addContacts(newContacts: Map<String, String>) {
        // Validate all contacts before adding any
        for ((name, phone) in newContacts) {
            if (name.isBlank()) throw kotlin.IllegalArgumentException("Contact name cannot be empty")
            if (!phone.matches(Regex("\\d{3}-\\d{4}"))) {
                throw kotlin.IllegalArgumentException("Phone number for $name must be in format 555-1234")
            }
        }

        contacts.putAll(newContacts)
        println("${newContacts.size} contacts added")
    }

    // Remove a contact
    fun removeContact(name: String): Boolean {
        return if (contacts.remove(name) != null) {
            println("Contact $name removed")
            true
        } else {
            println("Contact $name not found")
            false
        }
    }

    // Update a contact only if it exists
    fun updateContactIfExists(name: String, newPhoneNumber: String): Boolean {
        if (!newPhoneNumber.matches(Regex("\\d{3}-\\d{4}"))) {
            throw kotlin.IllegalArgumentException("Phone number must be in format 555-1234")
        }

        // Only update if the contact exists
        return if (name in contacts) {
            contacts[name] = newPhoneNumber
            println("Contact $name updated with new number $newPhoneNumber")
            true
        } else {
            println("Contact $name not found, no update performed")
            false
        }
    }

    // Search for contacts by partial name match
    fun searchByName(query: String): Map<String, String> {
        return contacts.filter { (name, _) ->
            name.contains(query, ignoreCase = true)
        }
    }

    // Find contacts with a specific area code
    fun findContactsByAreaCode(areaCode: String): Map<String, String> {
        return contacts.filter { (_, phoneNumber) ->
            phoneNumber.startsWith(areaCode)
        }
    }

    // Get contacts sorted by name
    fun getContactsSortedByName(): Map<String, String> {
        return contacts.toSortedMap()
    }

    // Get contacts sorted by phone number
    fun getContactsSortedByPhoneNumber(): Map<String, String> {
        return contacts.toList()
            .sortedBy { (_, phoneNumber) -> phoneNumber }
            .toMap()
    }

    // Group contacts by the first letter of their name
    fun groupContactsByFirstLetter(): Map<Char, List<String>> {
        return contacts.keys.groupBy { name ->
            name.first().uppercaseChar()
        }
    }

    // Count how many contacts have each area code
    fun countContactsByAreaCode(): Map<String, Int> {
        return contacts.values
            .groupingBy { phoneNumber ->
                phoneNumber.substringBefore('-')
            }
            .eachCount()
    }

    // Print a summary of all contacts
    fun printContactSummary() {
        println("Contact Summary:")
        println("Total Contacts: ${contacts.size}")

        // If we have contacts, print some details
        if (contacts.isNotEmpty()) {
            val firstAlphabetically = contacts.keys.minOrNull()
            val lastAlphabetically = contacts.keys.maxOrNull()

            println("First contact alphabetically: $firstAlphabetically")
            println("Last contact alphabetically: $lastAlphabetically")

            // Group by first letter and print count for each letter
            val groupedByFirstLetter = groupContactsByFirstLetter()
            println("Contacts by first letter:")
            groupedByFirstLetter.forEach { (letter, names) ->
                println("  $letter: ${names.size} contacts")
            }
        }
    }

    // Create a new contact group
    fun createContactGroup(groupName: String) {
        if (groupName.isBlank()) throw kotlin.IllegalArgumentException("Group name cannot be empty")

        contactGroups.putIfAbsent(groupName, mutableMapOf())
        println("Contact group '$groupName' created")
    }

    // Add an existing contact to a group
    fun addContactToGroup(name: String, groupName: String): Boolean {
        // Check if contact exists
        val phoneNumber = contacts[name]
        if (phoneNumber == null) {
            println("Contact $name does not exist")
            return false
        }

        // Check if group exists
        val group = contactGroups[groupName]
        if (group == null) {
            println("Group $groupName does not exist")
            return false
        }

        // Add contact to group
        group[name] = phoneNumber
        println("Added $name to group $groupName")
        return true
    }

    // Get all contacts in a group
    fun getContactsInGroup(groupName: String): Map<String, String>? {
        return contactGroups[groupName]
    }

    // Remove a contact from a group
    fun removeContactFromGroup(name: String, groupName: String): Boolean {
        val group = contactGroups[groupName] ?: return false
        return group.remove(name) != null
    }
}

fun main() {
    // Create a contact manager
    val contactManager = ContactManager()

    println("--- Adding individual contacts ---")
    try {
        contactManager.addContact("John Smith", "555-1234")
        contactManager.addContact("Jane Doe", "555-5678")
        contactManager.addContact("Alice Johnson", "555-9012")
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
    }

    println("\n--- Adding multiple contacts ---")
    try {
        val newContacts = mapOf(
            "Bob Brown" to "555-3456",
            "Charlie Davis" to "555-7890",
            "David Wilson" to "555-2345"
        )
        contactManager.addContacts(newContacts)
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
    }

    println("\n--- Retrieving contacts ---")
    println("Jane's number: ${contactManager.getPhoneNumber("Jane Doe")}")
    println("Eve's number: ${contactManager.getPhoneNumberOrMessage("Eve")}")

    try {
        println("Bob's number: ${contactManager.getPhoneNumberOrThrow("Bob Brown")}")
        println("Unknown number: ${contactManager.getPhoneNumberOrThrow("Unknown")}")
    } catch (e: NoSuchElementException) {
        println("Error: Contact not found")
    }

    println("\n--- Updating and removing contacts ---")
    contactManager.updateContactIfExists("Jane Doe", "555-8765")
    contactManager.updateContactIfExists("Unknown", "555-0000")
    contactManager.removeContact("Charlie Davis")
    contactManager.removeContact("Unknown")

    println("\n--- Searching and filtering ---")
    println("Contacts containing 'Jo':")
    val joContacts = contactManager.searchByName("Jo")
    joContacts.forEach { (name, number) -> println("$name: $number") }

    println("\nContacts with area code 555:")
    val areaCodeContacts = contactManager.findContactsByAreaCode("555")
    areaCodeContacts.forEach { (name, number) -> println("$name: $number") }

    println("\n--- Sorted contacts ---")
    println("Contacts sorted by name:")
    contactManager.getContactsSortedByName().forEach { (name, number) ->
        println("$name: $number")
    }

    println("\nContacts sorted by phone number:")
    contactManager.getContactsSortedByPhoneNumber().forEach { (name, number) ->
        println("$name: $number")
    }

    println("\n--- Working with contact groups ---")
    contactManager.createContactGroup("Family")
    contactManager.createContactGroup("Work")

    contactManager.addContactToGroup("John Smith", "Family")
    contactManager.addContactToGroup("Jane Doe", "Family")
    contactManager.addContactToGroup("Bob Brown", "Work")
    contactManager.addContactToGroup("David Wilson", "Work")

    println("\nFamily contacts:")
    contactManager.getContactsInGroup("Family")?.forEach { (name, number) ->
        println("$name: $number")
    }

    println("\nWork contacts:")
    contactManager.getContactsInGroup("Work")?.forEach { (name, number) ->
        println("$name: $number")
    }

    contactManager.removeContactFromGroup("Jane Doe", "Family")

    println("\nFamily contacts after removal:")
    contactManager.getContactsInGroup("Family")?.forEach { (name, number) ->
        println("$name: $number")
    }

    println("\n--- Contact summary ---")
    contactManager.printContactSummary()
}
