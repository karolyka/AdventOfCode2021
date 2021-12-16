import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
class Day16(inputFileName: String) : DayZero(inputFileName) {

    private lateinit var decoderState: DecoderState
    private lateinit var input: String

    override fun preprocess() {
        input = inputLines.first().map { it.hexToBin() }.joinToString("")
        decoderState = DecoderState(StringBuilder(input), mutableListOf())
        decode(decoderState)
    }

    private fun decode(coded: DecoderState) {
        coded.versions.add(coded.input.binToInt(3))

        when (val typeId = coded.input.binToInt(3)) {
            4 -> decodeNumbers(coded)
            else -> decodePacket(coded, typeId)
        }
    }

    private fun decodePacket(coded: DecoderState, typeId: Int) {
        if (coded.subPacket == null)
            coded.subPacket = SubPacket(typeId)
        else
            coded.subPacket!!.subPackets.add(SubPacket(typeId))
        val lengthType = if (coded.input.binToInt(1) == 0) 15 else 11
        var length = coded.input.binToInt(lengthType)
        while (length > 0) {
            val preLength = coded.input.length
            decode(coded)
            length -= if (lengthType == 15) preLength - coded.input.length else 1
        }
    }

    private fun decodeNumbers(coded: DecoderState) {
        var go = true
        if (coded.subPacket!!.subPackets.isEmpty())
            coded.subPacket!!.subPackets.add(SubPacket(0))
        val values = coded.subPacket!!.subPackets.last().subPackets
//        val values = coded.subPackets.last().values
        while (go) {
            go = coded.input.binToInt(1) == 1
            values.add(SubPacket(0, values = mutableListOf(coded.input.binToInt(4))))
//            values.add(coded.input.binToInt(4))
        }
    }

    override fun task1(): Long {
        decoderState.versions.sum().toLong()
            .also {
                println("Sum of version numbers: $it")
                return it
            }
    }

    override fun task2(): Long {
        decoderState.getValue().toLong()
            .also {
                println("Lowest risk (5x): $it")
                return it
            }
    }

}

private fun Char.hexToBin() = this.digitToInt(16).toString(2).padStart(4, '0')

private fun StringBuilder.binToInt(len: Int) = this.take(len).toString().toInt(2).also { this.delete(0, len) }

data class SubPacket(
    val typeId: Int,
    val subPackets: MutableList<SubPacket> = mutableListOf(),
    val values: MutableList<Int> = mutableListOf()
) {
    fun getValue(): Int = when (typeId) {
        0 -> subPackets.sumOf { it.getValue() } + values.sum()
        1 -> subPackets.fold(1) { acc, subPacket -> acc * subPacket.getValue() } *
                values.fold(1) { acc, i -> acc * i }
        2 -> min(subPackets.minOfOrNull { it.getValue() } ?: Int.MAX_VALUE, values.minOrNull() ?: Int.MAX_VALUE)
        3 -> max(subPackets.maxOfOrNull { it.getValue() } ?: 0, values.maxOrNull() ?: 0)
        5 -> if (subPackets.component1().getValue() > subPackets.component2().getValue()) 1 else 0
        6 -> if (subPackets.component1().getValue() < subPackets.component2().getValue()) 1 else 0
        7 -> if (subPackets.component1().getValue() == subPackets.component2().getValue()) 1 else 0
        else -> -1
    }
}

data class DecoderState(
    var input: StringBuilder,
    val versions: MutableList<Int>,
    var subPacket: SubPacket? = null
) {
    fun getValue(): Int = subPacket?.getValue() ?: Int.MAX_VALUE
}