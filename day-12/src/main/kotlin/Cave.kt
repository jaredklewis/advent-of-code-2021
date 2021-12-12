data class Cave(val id: String) {
    val isStart = id == "start"
    val isEnd = id == "end"
    val isSmall = !isStart && !isEnd && id.matches(Regex("^[a-z]+$"))
    val connections = mutableListOf<Cave>()
}

/**
 * Load the given resource file, parse the cave system,
 * and return the start cave.
 */
fun loadCaveSystem(resource: String): Cave {
    val text = object {}.javaClass.getResource(resource)?.readText() ?: ""

    val caves = mutableMapOf<String, Cave>()

    text.lines().filter { it.isNotEmpty() }.forEach { line ->
        val (leftId, rightId) = line.split("-")

        caves.putIfAbsent(leftId, Cave(leftId))
        caves.putIfAbsent(rightId, Cave(rightId))

        val leftCave = caves[leftId]
        val rightCave = caves[rightId]

        if (leftCave!!.connections.none { it.id == rightId }) {
            leftCave.connections.add(rightCave!!)
        }

        if (rightCave!!.connections.none { it.id == leftId }) {
            rightCave.connections.add(leftCave)
        }
    }

    return caves["start"]!!
}
