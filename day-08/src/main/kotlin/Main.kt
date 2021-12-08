// https://adventofcode.com/2021/day/8

fun countIdentifiableDigitsInOutput(entries: List<Entry>): Int {
    var count = 0;
    val unique = arrayOf(7, 4, 3, 2)
    for (entry in entries) {
        entry.output.forEach { it ->
            if (unique.contains(it.length)) {
                count += 1
            }
        }
    }

    return count
}

fun String.toCharSet(): Set<Char> {
    return this.toCharArray().toSet()
}

fun loadEntries(resource: String): List<Entry> {
    val text = object {}.javaClass.getResource(resource)?.readText() ?: ""

    return text
        .split("\n")
        .filter { line -> line.isNotEmpty() }
        .map(::parseEntry)
}


fun parseEntry(line: String): Entry {
    val parts = line.split(" | ")
    return Entry(
        signalPatterns = parts[0].split(" "),
        output = parts[1].split(" "),
    )
}

fun main() {
    // Data
    val example = loadEntries("example.txt");
    val entries = loadEntries("input.txt")

    // Part one example
    if (countIdentifiableDigitsInOutput(example) != 26) {
        throw AssertionError("Example incorrect.")
    }

    // Part one
    val identifiableDigitsInOutput = countIdentifiableDigitsInOutput(entries)
    println("Part one - identifiable digits in output: $identifiableDigitsInOutput")


    // Part two example
    val exampleSum = example.sumOf { it.decode() }
    if (exampleSum != 61229) {
        throw AssertionError("Part two example incorrect, got $exampleSum")
    }

    // Part two
    val sum = entries.sumOf { it.decode() }
    println("Part two - sum of decoded outputs: $sum")
}

