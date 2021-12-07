import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.absoluteValue
import kotlin.math.sign

@Suppress("unused")
class Day05(inputFileName: String) : DayZero(inputFileName) {
    private lateinit var lines: List<Line>
    private lateinit var matrix: List<IntArray>

    override fun preprocess() {
        lines = inputLines.map { Line.build(it) }
        val maxX = lines.maxOf { max(it.p1.x, it.p2.x) } + 1
        val maxY = lines.maxOf { max(it.p1.y, it.p2.y) } + 1
        matrix = (1..maxY).map {
            (1..maxX).map { 0 }.toIntArray()
        }.toList()
    }

    override fun task1(): Long {
        lines.forEach { line ->
            line.getPoints().forEach {
                matrix[it.y][it.x]++
            }
        }
        if (matrix.size < 20)
            println(matrix.draw())
        matrix.countOverlap().also {
            println("Overlapped (without diagonal): $it")
            return it.toLong()
        }
    }

    override fun task2(): Long {
        preprocess()
        lines.forEach { line ->
            line.getPoints(false).forEach {
                matrix[it.y][it.x]++
            }
        }
        if (matrix.size < 20)
            println(matrix.draw())
        matrix.countOverlap().also {
            println("Overlapped (with diagonal): $it")
            return it.toLong()
        }
    }
}

private fun List<IntArray>.draw(): String {
    return joinToString("\n") { line ->
        line.map { if (it == 0) '.' else "%d".format(it) }.joinToString("")
    }
}

private fun List<IntArray>.countOverlap(): Int {
    return sumOf { line -> line.count { it > 1 } }
}

data class Line(val p1: Point, val p2: Point) {
    companion object Builder {
        fun build(coords: String): Line {
            coords.split(' ').also {
                return Line(it.component1().toCoord(), it.component3().toCoord())
            }
        }

        private fun String.toCoord(): Point {
            split(',').also {
                return Point(it.component1().toInt(), it.component2().toInt())
            }
        }
    }

    fun getPoints(noDiagonal: Boolean = true): List<Point> {
        val xDiff = p2.x - p1.x
        val yDiff = p2.y - p1.y
        val xStep = xDiff.sign
        val yStep = yDiff.sign
        return when {
            (xDiff != 0 && yDiff == 0) -> (min(p1.x, p2.x)..max(p1.x, p2.x)).map { x -> Point(x, p1.y) }
            (xDiff == 0 && yDiff != 0) -> (min(p1.y, p2.y)..max(p1.y, p2.y)).map { y -> Point(p1.x, y) }
            (!noDiagonal && xDiff.absoluteValue == yDiff.absoluteValue) -> {
                var x = p1.x
                var y = p1.y
                (0..xDiff.absoluteValue).map {
                    Point(x, y).also {
                        x += xStep
                        y += yStep
                    }
                }
            }
            else -> listOf()
        }
    }
}

data class Point(val x: Int, val y: Int)
