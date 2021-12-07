private val splitter = " +".toRegex()

@Suppress("unused")
class Day04(inputFileName: String) : DayZero(inputFileName) {

    private lateinit var randomNumber: List<Int>
    private lateinit var bingoBoard: MutableList<BingoBoard>

    private var noThrow = false

    override fun preprocess() {
        bingoBoard = mutableListOf()
        lateinit var board: BingoBoard
        inputLines.map { it.trim() }.forEach { line ->
            when {
                line.contains(',') -> randomNumber = line.split(',').map { it.toInt() }
                line == "" -> board = BingoBoard(noThrow).also { bingoBoard.add(it) }
                else -> board.add(line)
            }
        }
    }

    override fun task1(): Long {
        try {
            randomNumber.forEach { bingoBoard.chosen(it) }
        } catch (e: WonException) {
            println(bingoBoard.filter { it.won }.sumOf { it.sumOfAllUnmarked() * it.lastNumber })
        }
        return 0
    }

    override fun task2(): Long {
        noThrow = true
        preprocess()
        randomNumber.forEach {
            bingoBoard.chosen(it) { board ->
                println(board.sumOfAllUnmarked() * board.lastNumber)
            }
        }
        return 0
    }
}

private fun MutableList<BingoBoard>.chosen(number: Int, wonAction: (BingoBoard) -> Unit = { }) {
    forEach { it.chosen(number, wonAction) }
}

class BingoBoard(private val noThrow: Boolean = false) {
    companion object {
        var counter = 0
    }

    data class Element(val index: Int, val number: Int, var done: Boolean = false)

    private val board: MutableList<MutableList<Element>> = mutableListOf()
    var won = false
    var lastNumber = 0
    private var wonNumber = 0

    fun add(line: String) {
        board.add(line.split(splitter).mapIndexed { index, s -> Element(index, s.toInt()) }.toMutableList())
    }

    fun chosen(number: Int, wonAction: (BingoBoard) -> Unit) {
        lastNumber = number
        val size = board.first().size
        board.forEach { line ->
            if (!won) {
                line.firstOrNull { it.number == number }?.also { element ->
                    element.done = true
                    won = (size == line.filter { it.done }.size)
                            || (size == board.map { elements -> elements.first { it.index == element.index } }
                        .filter { it.done }.size)
                    if (won) {
                        wonNumber = ++counter
                        wonAction(this)
                        if (!noThrow) throw WonException()
                    }
                }
            }
        }
    }

    fun sumOfAllUnmarked() = board.sumOf { line -> line.filter { !it.done }.sumOf { it.number } }
}

class WonException : Throwable()
