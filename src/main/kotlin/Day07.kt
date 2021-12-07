import kotlin.math.absoluteValue

@Suppress("unused")
class Day07(inputFileName: String) : DayZero(inputFileName) {
    private var positions = intArrayOf()

    override fun preprocess() {
        positions = inputLines.first().split(',').map { it.toInt() }.toIntArray()
    }

    override fun task1(): Long {
        calculate { i1, i2 -> (i1 - i2).absoluteValue }.also { last ->
            println("Total cost: $last")
            return last.toLong()
        }
    }

    override fun task2(): Long {
        calculate { i1, i2 -> (i1 - i2).absoluteValue * (i1 - i2).absoluteValue.plus(1) / 2 }.also { last ->
            println("Total cost: $last")
            return last.toLong()
        }
    }

    private fun calculate(algorithm: (Int, Int) -> Int): Int {
        val mini = positions.minOrNull() ?: 0
        val maxi = positions.maxOrNull() ?: 0
        var last = Int.MAX_VALUE
        var current: Int
        (mini..maxi).forEach { number ->
            current = positions.sumOf { algorithm(it, number) }
            if (current < last)
                last = current
            else if (current > last) {
                return@forEach
            }
            println("$number -> $current")

        }
        return last
    }
}