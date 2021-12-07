@Suppress("unused")
class Day06(inputFileName: String) : DayZero(inputFileName) {
    private var initial = intArrayOf()

    override fun preprocess() {
        initial = inputLines.first().substringAfter(':').trim().split(',').map { it.toInt() }.toIntArray()
    }

    override fun task1(): Long {
        countFish(80).also {
            println("Fish population: $it")
            return it
        }
    }

    override fun task2(): Long {
        countFish(256).also {
            println("Fish population: $it")
            return it
        }
    }

    private fun countFish(days: Int): Long {
        val lanternFish = (0..8).map { age -> LanternFish(age, initial.filter { it == age }.size.toLong()) }
        repeat(days) {
            lanternFish.first { it.age == 0 }.also { reproduce ->
                if (reproduce.number > 0)
                    lanternFish.first { it.age == 7 }.also { it.number += reproduce.number }
                reproduce.age = 9
            }
            lanternFish.forEach { it.age-- }
        }
        return lanternFish.sumOf { it.number }
    }
}

data class LanternFish(var age: Int, var number: Long)