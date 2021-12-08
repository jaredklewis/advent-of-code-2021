val DIGITS = mapOf(
    "cf" to "1",      // 2 segments

    "acf" to "7",     // 3 segments

    "bcdf" to "4",    // 4 segments

    "acdeg" to "2",   // 5 segments
    "acdfg" to "3",   // 5 segments
    "abdfg" to "5",   // 5 segments

    "abcefg" to "0",  // 6 segments
    "abdefg" to "6",  // 6 segments
    "abcdfg" to "9",  // 6 segments

    "abcdefg" to "8", // 7 segments
)

data class Entry(val signalPatterns: List<String>, val output: List<String>) {
    fun decode(): Int {
        // Length 2
        val cf = charsForSignalPatternWithLength(2)

        // Length 3
        val acf = charsForSignalPatternWithLength(3)

        // Length 4
        val bcdf = charsForSignalPatternWithLength(4)

        // Length 5
        val length5PatternCharsByCount = charsByCountForSignalPatternsWithLength(5)
        val adg = length5PatternCharsByCount[3]!!
        val be = length5PatternCharsByCount[1]!!


        // Length 6
        val length6PatternCharsByCount = charsByCountForSignalPatternsWithLength(6)
        val abfg = length6PatternCharsByCount[3]!!
        val cde = length6PatternCharsByCount[2]!!

        val possible = mapOf(
            'a' to (acf intersect adg intersect abfg),
            'b' to (bcdf intersect be intersect abfg),
            'c' to (bcdf intersect acf intersect cf intersect cde),
            'd' to (bcdf intersect cde),
            'e' to (be intersect cde),
            'f' to (bcdf intersect acf intersect cf intersect abfg),
            'g' to (adg intersect abfg),
        )

        val found = possible.values.filter { it.size == 1 }.fold(mutableSetOf<Char>()) { set, target ->
            set.addAll(target)
            set
        }

        val decoder = possible.mapValues {
            if (it.value.size == 1) {
                it.value.first()
            } else {
                it.value.first { char -> char !in found }
            }
        }.entries.associateBy({ it.value }) { it.key }

        val decodedOutputs = output.map {
            it.map { char -> decoder[char] }
                .joinToString("")
                .toCharArray()
                .sorted()
                .joinToString("")
        }


        return decodedOutputs.mapNotNull { DIGITS[it] }
            .joinToString("") { it }
            .toInt()
    }

    private fun charsForSignalPatternWithLength(length: Int): Set<Char> {
        return this.signalPatterns.first { it.length == length }.toCharSet()
    }

    private fun charsByCountForSignalPatternsWithLength(length: Int): Map<Int, Set<Char>> {
        val combinedPatterns = this.signalPatterns
            .filter { it.length == length }
            .joinToString("") { it }

        return combinedPatterns
            .toCharSet()
            .groupBy { char -> combinedPatterns.count { it == char } }
            .mapValues { it.value.toSet() }
    }
}
