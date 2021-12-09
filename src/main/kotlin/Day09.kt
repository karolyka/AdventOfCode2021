@Suppress("unused")
class Day09(inputFileName: String) : DayZero(inputFileName) {
    private var columnNumber = 0
    private var rowNumber = 0
    private lateinit var lowPointMap: MutableSet<Pair<Int, Int>>

    override fun preprocess() {
        columnNumber = inputLines.first().length
        rowNumber = inputLines.size
        lowPointMap = mutableSetOf()
    }

    override fun task1(): Long {
        forEachNumber { row, column ->
            if (isMin(row, column)) {
                lowPointMap.add(Pair(row, column))
                inputLines[row][column].digitToInt().plus(1)
            } else
                0
        }.also {
            println("Risk level: $it")
            return it.toLong()
        }
    }

    override fun task2(): Long {
        lowPointMap.map {
            getBasin(it)
        }
            .sortedDescending()
            .take(3)
            .reduce { acc, i -> acc * i }
            .also {
                println("Basin (multiply the 3 largest): $it")
                return it.toLong()
            }
    }

    private fun forEachNumber(calc: (Int, Int) -> Int) = (0 until columnNumber).map { column ->
        (0 until rowNumber).map { row ->
            calc(row, column)
        }.sumOf { it }
    }.sumOf { it }

    private fun isMin(row: Int, column: Int) = inputLines[row][column].let { number ->
        (row == 0 || number < inputLines[row - 1][column]) &&
                (row == rowNumber - 1 || number < inputLines[row + 1][column]) &&
                (column == 0 || number < inputLines[row][column - 1]) &&
                (column == columnNumber - 1 || number < inputLines[row][column + 1])
    }

    private fun getBasin(coords: Pair<Int, Int>, map: MutableSet<Pair<Int, Int>> = mutableSetOf()): Int {
        return if (isNotValid(coords, map))
            0
        else
            with(coords) {
                1 +
                        getBasin(Pair(first - 1, second), map) +
                        getBasin(Pair(first + 1, second), map) +
                        getBasin(Pair(first, second - 1), map) +
                        getBasin(Pair(first, second + 1), map)
            }
    }

    private fun isNotValid(coords: Pair<Int, Int>, map: MutableSet<Pair<Int, Int>> = mutableSetOf()) =
        if (!map.contains(coords)) {
            map.add(coords)
            with(coords) {
                first < 0 || first >= rowNumber || second < 0 || second >= columnNumber || inputLines[first][second] == '9'
            }
        } else true

}