package functions

data class TeamMember(val name: String, val role: String)

fun main() {
    val team = listOf(
        TeamMember("Alice", "Developer"),
        TeamMember("Bob", "Designer"),
        TeamMember("Charlie", "Developer"),
        TeamMember("Dave", "Manager"),
        TeamMember("Eve", "Tester"),
        TeamMember("Frank", "Designer")
    )

    println("Original team:")
    team.forEach { println(it) }

    // Filter out all Designers using a lambda with a local return
    val filteredTeam = mutableListOf<TeamMember>()
    team.forEach {
        if (it.role == "Designer") {
            return@forEach // Local return to skip this member
        }
        filteredTeam.add(it)
    }

    println("\nTeam after filtering out Designers (using lambda):")
    filteredTeam.forEach { println(it) }

    // Custom label to filter out all Managers
    val teamWithoutManagers = mutableListOf<TeamMember>()
    team.forEach excludeManagers@{
        if (it.role == "Manager") {
            return@excludeManagers // Local return with custom label to skip this member
        }
        teamWithoutManagers.add(it)
    }

    println("\nTeam after filtering out Managers (using custom label):")
    teamWithoutManagers.forEach { println(it) }

    // Using an anonymous function to filter out Testers
    val teamWithoutTesters = mutableListOf<TeamMember>()
    team.forEach(
        fun(member) {
            if (member.role == "Tester") {
                return // Local return from anonymous function to skip this member
            }
            teamWithoutTesters.add(member)
        }
    )

    println("\nTeam after filtering out Testers (using anonymous function):")
    teamWithoutTesters.forEach { println(it) }
}
