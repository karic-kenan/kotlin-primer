package oop

import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.collections.set
import kotlin.jvm.java
import kotlin.text.isEmpty

// Marks a class as serializable to JSON.
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class JsonSerializable

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class JsonField(val name: String)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Required

@JsonSerializable
data class CustomUser(
    @JsonField("user_name") @Required val username: String,
    @JsonField("email_address") @Required val email: String,
    @JsonField("age") val age: Int,
    @JsonField("user_address") var address: String? = null
)

// This function serializes the annotated class to JSON, with options to skip or include null values
fun processAnnotations(obj: Any, skipNulls: Boolean = false): String {
    val clazz = obj::class

    if (!clazz.java.isAnnotationPresent(JsonSerializable::class.java)) {
        throw kotlin.IllegalArgumentException("The class ${clazz.simpleName} is not annotated with @JsonSerializable")
    }

    val fields = clazz.java.declaredFields
    val jsonMap = mutableMapOf<String, Any?>()

    for (field in fields) {
        field.isAccessible = true

        // Process JsonField annotation
        val jsonFieldAnnotation = field.getAnnotation(JsonField::class.java)
        if (jsonFieldAnnotation != null) {
            val fieldName = jsonFieldAnnotation.name
            val value = field.get(obj)

            // Handle null or blank values based on the skipNulls flag
            if (value == null && skipNulls) {
                // Skip the field if null values should be skipped
                continue
            } else {
                jsonMap[fieldName] = value ?: "null"  // Assign "null" string if value is null
            }
        }
    }

    return jsonMap.map { (key, value) -> "\"$key\": \"${value}\"" }
        .joinToString(prefix = "{", postfix = "}")
}

// Validates fields annotated with @Required
fun validate(obj: Any) {
    val clazz = obj::class.java

    for (field in clazz.declaredFields) {
        field.isAccessible = true
        if (field.isAnnotationPresent(Required::class.java)) {
            val value = field.get(obj)
            if (value == null || (value is String && value.isEmpty())) {
                throw kotlin.IllegalArgumentException("Field ${field.name} is required but was null or empty")
            }
        }
    }
    println("Object validated successfully")
}

fun main() {
    val user1 = CustomUser(
        username = "john_doe",
        email = "john@example.com",
        age = 30,
        address = "123 Main St"
    )

    validate(user1)

    val json1 = processAnnotations(user1)
    println("JSON for user1: $json1")

    try {
        val user2 = CustomUser(
            username = "jane_doe",
            email = "jane@example.com",
            age = 25,
            address = null  // This is required but null
        )

        validate(user2)
        val json2 = processAnnotations(user2)
        println("JSON for user2: $json2")
    } catch (e: IllegalArgumentException) {
        println("Validation error: ${e.message}")
    }
}
