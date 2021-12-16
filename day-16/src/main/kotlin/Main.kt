// https://adventofcode.com/2021/day/16

fun main() {
    if ("110100101111111000101000" != parseHex("D2FE28").toBitString()) {
        throw error("Expected these to match.")
    }

    val exampleLiteral = parseTopPacket("D2FE28")
    if (exampleLiteral !is LiteralPacket) {
        throw error("Expected literal packet type")
    }

    if (exampleLiteral.value != 2021L) {
        throw error("Expected literal value of 2021")
    }

    if (exampleLiteral.version != 6) {
        throw error("Expected version of 6")
    }

    if (sumVersions(parseTopPacket("8A004A801A8002F478")) != 16) {
        throw error("Expected sum version of 16")
    }

    if (sumVersions(parseTopPacket("620080001611562C8802118E34")) != 12) {
        throw error("Expected sum version of 12")
    }

    if (sumVersions(parseTopPacket("C0015000016115A2E0802F182340")) != 23) {
        throw error("Expected sum version of 23")
    }

    if (sumVersions(parseTopPacket("A0016C880162017C3686B18A3D4780")) != 31) {
        throw error("Expected sum version of 31")
    }

    val input = loadResourceAsString("input.txt")
    val packet = parseTopPacket(input)
    println("Part one - sum of packet versions: ${sumVersions(packet)}")


    if (parseTopPacket("C200B40A82").value != 3L) {
        throw error("Expected sum version of 3")
    }

    if (parseTopPacket("04005AC33890").value != 54L) {
        throw error("Expected sum version of 54")
    }

    if (parseTopPacket("880086C3E88112").value != 7L) {
        throw error("Expected sum version of 23")
    }

    if (parseTopPacket("CE00C43D881120").value != 9L) {
        throw error("Expected sum version of 31")
    }

    if (parseTopPacket("D8005AC2A8F0").value != 1L) {
        throw error("Expected sum version of 31")
    }

    println("Part two - packet value: ${packet.value}")
}

fun sumVersions(topPacket: Packet): Int {
    var sum = topPacket.version

    if (topPacket is OperatorPacket) {
        topPacket.subPackets.forEach { packet ->
            sum += sumVersions(packet)
        }
    }

    return sum
}

fun loadResourceAsString(resource: String): String {
    return object {}.javaClass.getResource(resource)?.readText()?.trim() ?: ""
}
