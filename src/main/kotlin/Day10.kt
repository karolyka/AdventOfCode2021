data class Chunk(
    val code: Char,
    val open: Boolean = true,
    var pair: Char = ' ',
    var value: Int = 0,
    var autoCompleteValue: Int = 0
)

@Suppress("unused")
class Day10(inputFileName: String) : DayZero(inputFileName) {
    private lateinit var openChunks: MutableMap<Char, Chunk>
    private lateinit var closeChunks: MutableMap<Char, Chunk>

    override fun preprocess() {
        openChunks = mutableMapOf()
        closeChunks = mutableMapOf()
        listOf(
            Chunk('(', autoCompleteValue = 1),
            Chunk('[', autoCompleteValue = 2),
            Chunk('{', autoCompleteValue = 3),
            Chunk('<', autoCompleteValue = 4),
            Chunk(')', false, '(', 3),
            Chunk(']', false, '[', 57),
            Chunk('}', false, '{', 1197),
            Chunk('>', false, '<', 25137)
        ).forEach {
            if (it.open)
                openChunks[it.code] = it
            else
                closeChunks[it.code] = it
        }
    }

    override fun task1(): Long {
        inputLines.map { it.isChunksValid() }
            .filter { !it.first }
            .sumOf { closeChunks[it.second]!!.value }
            .also {
                println("Score: $it")
                return it.toLong()
            }
    }

    override fun task2(): Long {
        inputLines
            .filter { it.isChunksValid().first }
            .map { it.getRepairChunksScore() }
            .sorted()
            .let { it[it.size / 2] }
            .also {
                println("Middle score: $it")
                return it
            }
    }

    private fun String.isChunksValid(): Pair<Boolean, Char> {
        val chunks = mutableListOf<Chunk>()
        map { it.toChunk() }
            .forEach { chunk ->
                if (chunk.open) {
                    chunks.add(chunk)
                } else {
                    if (!chunks.last().let { it.open && chunk.pair == it.code })
                        return Pair(false, chunk.code)
                    chunks.removeLast()
                }
            }
        return Pair(true, ' ')
    }

    private fun String.getRepairChunksScore(): Long {
        val chunks = mutableListOf<Chunk>()
        map { it.toChunk() }
            .forEach { chunk ->
                if (chunk.open) {
                    chunks.add(chunk)
                } else {
                    chunks.removeLast()
                }
            }
        return chunks.reversed().fold(0L) { score, chunk -> score * 5 + chunk.autoCompleteValue }
    }

    private fun Char.toChunk() = openChunks[this] ?: closeChunks[this]!!
}
