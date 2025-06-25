package functions

object InfixFunctions {

    // Added a sealed class hierarchy for better type safety
    sealed class Permission(val name: String) {
        object Read : Permission("READ")
        object Write : Permission("WRITE")
        object Delete : Permission("DELETE")
        object Admin : Permission("ADMIN")

        // Custom implementation for better string representation
        override fun toString(): String = name
    }

    class Role(val name: String) {
        val permissions = mutableSetOf<Permission>()

        // Infix function to add a permission
        infix fun grant(permission: Permission): Role {
            permissions.add(permission)
            return this
        }

        // Infix function to add multiple permissions at once
        infix fun grantAll(newPermissions: Collection<Permission>): Role {
            permissions.addAll(newPermissions)
            return this
        }

        // Infix function to remove a permission
        infix fun revoke(permission: Permission): Role {
            permissions.remove(permission)
            return this
        }

        // Infix function to check if a permission exists
        infix fun can(permission: Permission): Boolean {
            return permissions.contains(permission)
        }

        // Infix function for comparing roles
        infix fun hasMorePermissionsThan(other: Role): Boolean {
            return permissions.size > other.permissions.size
        }

        fun showPermissions(): String = permissions.joinToString()
    }

    // Extension infix function outside the class
    infix fun Role.transferPermissionsTo(other: Role): Role {
        other grantAll this.permissions
        return other
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // Using the sealed class for type-safe permissions
        val adminRole = Role("Admin")
        val userRole = Role("User")
        val guestRole = Role("Guest")

        // Using infix functions to manage permissions with cleaner syntax
        adminRole grant Permission.Read
        adminRole grant Permission.Write
        adminRole grant Permission.Delete
        adminRole grant Permission.Admin

        println("Admin role permissions: ${adminRole.showPermissions()}")

        // Using infix to grant multiple permissions at once
        userRole grantAll listOf(Permission.Read, Permission.Write)
        println("User role permissions: ${userRole.showPermissions()}")

        // Only grant read permission to guest
        guestRole grant Permission.Read
        println("Guest role permissions: ${guestRole.showPermissions()}")

        // Checking permissions with infix notation
        println("Can admin delete? ${adminRole can Permission.Delete}")
        println("Can user delete? ${userRole can Permission.Delete}")

        // Using infix function to compare roles
        println("Admin has more permissions than User? ${adminRole hasMorePermissionsThan userRole}")
        println("User has more permissions than Guest? ${userRole hasMorePermissionsThan guestRole}")

        // Using external infix extension function
        println("\nTransferring permissions from Admin to Guest")
        adminRole transferPermissionsTo guestRole
        println("Guest role permissions after transfer: ${guestRole.showPermissions()}")

        // Revoking permissions with infix
        guestRole revoke Permission.Admin
        println("Guest role after revoking Admin: ${guestRole.showPermissions()}")
    }
}