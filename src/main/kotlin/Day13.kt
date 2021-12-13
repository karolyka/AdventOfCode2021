@Suppress("unused")
class Day13(inputFileName: String) : DayZero(inputFileName) {

    private lateinit var foldes: List<Pair<String, Int>>
    private lateinit var coordinates: Set<Coordinate>

    override fun preprocess() {
        coordinates = inputLines.filter { !it.startsWith('f') && it.isNotBlank() }
            .map { it.split(',') }
            .map { Coordinate(it.component1().toInt(), it.component2().toInt()) }
            .toSet()
        foldes = inputLines.filter { it.startsWith('f') }
            .map { it.substringAfterLast(' ') }
            .map { it.split('=') }
            .map { it.component1() to it.component2().toInt() }
    }

    override fun task1(): Long {
        doFold(1)
            .also {
                println("Visible: $it")
                return it.toLong()
            }
    }

    override fun task2(): Long {
        doFold()
            .also {
                println("Infrared: $it")
                return it.toLong()
            }
    }

    private fun doFold(maxFolds: Int = Int.MAX_VALUE): Int {
        foldes.forEachIndexed { index, fold ->
            if (index >= maxFolds)
                return@forEachIndexed
            printPaper()
            when (fold.first) {
                "x" -> coordinates.filter { it.x > fold.second }.forEach { it.x = fold.second - (it.x - fold.second) }
                "y" -> coordinates.filter { it.y > fold.second }.forEach { it.y = fold.second - (it.y - fold.second) }
            }
            coordinates = coordinates.toSet()
        }
        printPaper()
        return coordinates.size
    }

    private fun getDimensions() = coordinates.maxOf { it.x } to coordinates.maxOf { it.y }

    private fun printPaper() {
        println()
        if (coordinates.size < 110) {
            val (columns, rows) = getDimensions()
            (0..rows).forEach { row ->
                (0..columns).map { column ->
                    (coordinates.any { it.x == column && it.y == row }).let {
                        if (it) {
                            '#'
                        } else
                            ' '
                    }
                }.also {
                    if (it.size < 100)
                        println(it.joinToString(""))
                }
            }
        }
        println("Visible dots: ${coordinates.size}")
    }

}

data class Coordinate(var x: Int, var y: Int)