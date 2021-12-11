@Suppress("unused")
class Day11(inputFileName: String) : DayZero(inputFileName) {
    private lateinit var matrix: Array<ByteArray>
    private var rows = 0
    private var columns = 0

    override fun preprocess() {
        matrix =
            inputLines.map { line -> line.map { char -> char.code.minus(48).toByte() }.toByteArray() }.toTypedArray()
        rows = matrix.size
        columns = matrix.first().size
    }

    override fun task1(): Long {
        (1..100).sumOf { step() }.also {
            println("Flashes: $it")
            return it
        }
    }

    override fun task2(): Long {
        preprocess()
        var steps = 0L
        while (true) {
            steps++
            step()
            if (matrix.maxOfOrNull {
                    it.maxOrNull()!!
                } == 0.toByte())
                break
        }
        steps.also {
            println("Step of sync flash: $it")
            return it
        }
    }

    private fun step(): Long {
        var doFlash = false
        var sumFlashed = 0L
        matrix.forEach { row, column -> doFlash = (++matrix[row][column] > 9) || doFlash }
        if (doFlash) {
            var flashed = 1
            while (flashed > 0) {
                flashed = 0
                matrix.forEach { row, column -> flashed += flash(row, column) }
            }
            matrix.forEach { row, column ->
                if (matrix[row][column] == 0.toByte()) {
                    sumFlashed++
                }
            }
        }
        return sumFlashed
    }

    private fun flash(row: Int, column: Int): Int {
        matrix[row][column].let {
            return if (it > 9) {
                matrix[row][column] = 0
                (-1..1).sumOf { rowDelta ->
                    (-1..1).sumOf { columnDelta ->
                        if (rowDelta != 0 || columnDelta != 0)
                            incAdjacent(row + rowDelta, column + columnDelta)
                        else 0
                    }
                }
            } else
                0
        }
    }

    private fun incAdjacent(row: Int, column: Int) =
        if (row in 0 until rows &&
            column in 0 until columns
            && matrix[row][column] != 0.toByte()
        ) {
            matrix[row][column]++
            1
        } else
            0

    private fun Array<ByteArray>.forEach(action: (Int, Int) -> Unit) {
        val columns = first().size
        (indices).forEach { row ->
            (0 until columns).forEach { column ->
                action(row, column)
            }
        }
    }

}
