typealias BitArray = ByteArray

val lettersToBitArrays = mapOf(
    '0' to byteArrayOf(0, 0, 0, 0),
    '1' to byteArrayOf(0, 0, 0, 1),
    '2' to byteArrayOf(0, 0, 1, 0),
    '3' to byteArrayOf(0, 0, 1, 1),
    '4' to byteArrayOf(0, 1, 0, 0),
    '5' to byteArrayOf(0, 1, 0, 1),
    '6' to byteArrayOf(0, 1, 1, 0),
    '7' to byteArrayOf(0, 1, 1, 1),
    '8' to byteArrayOf(1, 0, 0, 0),
    '9' to byteArrayOf(1, 0, 0, 1),
    'A' to byteArrayOf(1, 0, 1, 0),
    'B' to byteArrayOf(1, 0, 1, 1),
    'C' to byteArrayOf(1, 1, 0, 0),
    'D' to byteArrayOf(1, 1, 0, 1),
    'E' to byteArrayOf(1, 1, 1, 0),
    'F' to byteArrayOf(1, 1, 1, 1)
)

fun parseHex(hexString: String): BitArray {
    var ba = byteArrayOf()

    for (char in hexString) {
        ba += lettersToBitArrays[char] ?: throw error("Unknown hex letter $char")
    }

    return ba
}

fun BitArray.toBitString(): String {
    val sb = StringBuilder()
    for (byte in this) {
        if (byte > 0) {
            sb.append("1")
        } else {
            sb.append("0")
        }
    }

    return sb.toString()
}

enum class LengthType {
    BIT_LENGTH, SUB_PACKET_NUMBER
}

enum class Operation {
    SUM, PRODUCT, MIN, MAX, GT, LT, EQUAL
}

const val SET_BIT: Byte = 1
const val CLEAR_BIT: Byte = 0

fun BitArray.toInt(): Int {
    return this.toBitString().toInt(2)
}

fun BitArray.toLong(): Long {
    return this.toBitString().toLong(2)
}

fun parseTopPacket(hexString: String): Packet {
    println("Parsing $hexString")
    val result = parsePacket(parseHex(hexString))

    if (result.tail.isNotEmpty() && result.tail.any { it == SET_BIT }) {
        throw error("Unexpected tail on top level packet: ${result.tail.toBitString()}")
    }

    return result.packet
}


class ParseResult(val packet: Packet, val tail: BitArray)

fun parsePackets(bitArray: BitArray): List<Packet> {
    val packets = mutableListOf<Packet>()
    var tail = bitArray.copyOf()
    while (true) {
        val result = parsePacket(tail)
        packets.add(result.packet)
        if (result.tail.isEmpty()) {
            break
        }
        tail = result.tail
    }

    return packets
}

fun parseNPackets(bitArray: BitArray, n: Int = 0): Pair<List<Packet>, BitArray> {
    val packets = mutableListOf<Packet>()
    var tail: BitArray = bitArray.copyOf()

    for (i in 1..n) {
        val result = parsePacket(tail)
        tail = result.tail
        packets.add(result.packet)
    }

    return Pair(packets, tail)
}

fun parsePacket(bitArray: BitArray): ParseResult {
    val version: Int = bitArray.copyOfRange(0, 3).toInt()
    val typeID = bitArray.copyOfRange(3, 6).toInt()

    if (typeID == 4) {
        var i = 6
        var literalBitArray: BitArray = byteArrayOf()
        while (true) {
            literalBitArray += bitArray.copyOfRange(i + 1, i + 5)
            val lastByte = bitArray[i] == CLEAR_BIT
            i += 5
            if (lastByte) {
               break
            }
        }
        val literal = literalBitArray.toLong()

        return ParseResult(
            packet = LiteralPacket(
                bitArray = bitArray,
                version = version,
                value = literal
            ),
            tail = bitArray.copyOfRange(i, bitArray.size)
        )

    }

    val lengthType = if (bitArray[6] == SET_BIT) LengthType.SUB_PACKET_NUMBER else LengthType.BIT_LENGTH

    val operator = when (typeID) {
        0 -> Operation.SUM
        1 -> Operation.PRODUCT
        2 -> Operation.MIN
        3 -> Operation.MAX
        5 -> Operation.GT
        6 -> Operation.LT
        7 -> Operation.EQUAL
        else -> throw error("Unknown type id $typeID")
    }

    if (lengthType == LengthType.BIT_LENGTH) {
        val lengthInBits = bitArray.copyOfRange(7, 22).toInt()
        val endOfSubPackets = 22 + lengthInBits
        val tail = if (bitArray.size > endOfSubPackets) {
            bitArray.copyOfRange(endOfSubPackets, bitArray.size)
        } else {
            byteArrayOf()
        }
        val subPackets = parsePackets(bitArray.copyOfRange(22, endOfSubPackets))

        return ParseResult(
            packet = OperatorPacket(
                bitArray = bitArray,
                version = version,
                subPackets = subPackets,
                value = evalValue(operator, subPackets)
            ),
            tail = tail
        )
    }

    val lengthInPackets = bitArray.copyOfRange(7, 18).toInt()
    val (subPackets, tail) = parseNPackets(bitArray.copyOfRange(18, bitArray.size), lengthInPackets)
    return ParseResult(
        OperatorPacket(
            bitArray = bitArray,
            version = version,
            value = evalValue(operator, subPackets),
            subPackets = subPackets

        ),
        tail = tail
    )
}

fun evalValue(operation: Operation, subPackets: List<Packet>): Long {
    val values: List<Long> = subPackets.map { it.value }
    return when (operation) {
        Operation.SUM -> values.sum()
        Operation.PRODUCT -> values.reduce { a, b -> a * b }
        Operation.MIN -> values.minOf { it }
        Operation.MAX -> values.maxOf { it }
        Operation.GT -> {
            if (values.size != 2) {
                throw error("Greater than packet with ${values.size} sub packets")
            }
            if (values.first() > values.last()) 1L else 0L
        }
        Operation.LT -> {
            if (values.size != 2) {
                throw error("Less than packet with ${values.size} sub packets")
            }
            if (values.first() < values.last()) 1L else 0L
        }
        Operation.EQUAL -> {
            if (values.size != 2) {
                throw error("Equal to packet with ${values.size} sub packets")
            }
            if (values.first() == values.last()) 1L else 0L
        }
    }
}


sealed class Packet {
    abstract val bitArray: BitArray
    abstract val version: Int
    abstract val value: Long
}

class LiteralPacket(
    override val bitArray: BitArray,
    override val version: Int,
    override val value: Long
) : Packet()

class OperatorPacket(
    override val bitArray: BitArray,
    override val version: Int,
    override val value: Long,
    val subPackets: List<Packet>

) : Packet()
