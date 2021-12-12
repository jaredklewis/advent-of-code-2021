// https://adventofcode.com/2021/day/12

typealias SmallCaveEvaluator = (connection: Cave, pathSoFar: List<Cave>) -> Boolean

fun calcPaths(startCave: Cave, determineIfSmallCaveEligible: SmallCaveEvaluator): Int {
    val paths = mutableSetOf<List<Cave>>()


    fun explore(cave: Cave, pathSoFar: List<Cave>) {
        cave.connections.forEach { connection ->
            val potentialPath = pathSoFar + listOf(connection)

            if (connection.isStart) {
                return@forEach
            }

            if (connection.isEnd) {
                paths.add(potentialPath)
                return@forEach
            }

            if (connection.isSmall && !determineIfSmallCaveEligible(connection, pathSoFar) ) {

                return@forEach
            }

            explore(connection, potentialPath)
        }
    }

    explore(startCave, listOf(startCave))

    return paths.size
}

fun calcPathsPartOne(startCave: Cave): Int {
    return calcPaths(startCave) { connection, pathSoFar ->
        !pathSoFar.contains(connection)
    }
}

fun calcPathsPartTwo(startCave: Cave): Int {
    return calcPaths(startCave) { connection, pathSoFar ->
        val countsPerLetter = pathSoFar
            .filter { it.isSmall }
            .groupingBy { it }
            .eachCount()

        val hasVisitedSmallCaveTwice = countsPerLetter.values.any { it > 1 }

        !hasVisitedSmallCaveTwice || !pathSoFar.contains(connection)
    }
}

fun main() {
    val smallExample = loadCaveSystem("small-example.txt")
    val mediumExample = loadCaveSystem("medium-example.txt")
    val largeExample = loadCaveSystem("large-example.txt")
    val startCave = loadCaveSystem("input.txt")

    // Part one
    assertPathCount("Part one small example", 10) {
        calcPathsPartOne(smallExample)
    }
    assertPathCount("Part one medium example", 19) {
        calcPathsPartOne(mediumExample)
    }
    assertPathCount("Part one large example", 226) {
        calcPathsPartOne(largeExample)
    }
    val partOneTotalPaths = calcPathsPartOne(startCave)
    println("Part one - total paths: $partOneTotalPaths")


    // Part two
    assertPathCount("Part two small example", 36) {
        calcPathsPartTwo(smallExample)
    }
    assertPathCount("Part two medium example", 103) {
        calcPathsPartTwo(mediumExample)
    }
    assertPathCount("Part two large example", 3509) {
        calcPathsPartTwo(largeExample)
    }
    val partTwoTotalPaths = calcPathsPartTwo(startCave)
    println("Part two - total paths: $partTwoTotalPaths")
}

private fun assertPathCount(label: String, expected: Int, block: () -> Int) {
    val actual = block()

    if (expected != actual) {
        throw AssertionError("$label incorrect. Expected: $expected - Actual: $actual")
    }
}
