@Suppress("unused")
class Day03(inputFileName: String) : DayZero(inputFileName) {

    override fun task1(): Long {
        mostCommonValues().also { mcv ->
            val gamma = mcv.toInt(2)
            val epsilon = mcv.invert().toInt(2)
            (gamma * epsilon).also {
                println("Gamma: $gamma, Epsilon: $epsilon, Power consumption: $it")
                return it.toLong()
            }
        }
    }

    override fun task2(): Long {
        val oxygen = filterNumbers(inputLines)
        val co2 = filterNumbers(inputLines) { it.invert() }
        (oxygen * co2).also {
            println("Oxygen: $oxygen, CO2: $co2, Life support rating: $it")
            return it.toLong()
        }
    }

    private fun filterNumbers(numbers: List<String>, map: (String) -> String = { it }): Int {
        var filteredNumbers = numbers
        var index = 0
        while (filteredNumbers.size > 1) {
            map(mostCommonValues(filteredNumbers)).also { mcv ->
                filteredNumbers = filteredNumbers.filter { it[index] == mcv[index] }
            }
            index++
        }
        return filteredNumbers.first().toInt(2)
    }

    private fun mostCommonValues(numbers: List<String> = inputLines): String {
        val sum = numbers.first().map { 0 }.toIntArray()
        numbers.forEach { it.forEachIndexed { index, c -> if (c == '1') sum[index]++ } }
        (numbers.size / 2.0).also { size ->
            return sum.map { if (it >= size) 1 else 0 }.joinToString("")
        }
    }

    private fun String.invert() = map { if (it == '1') '0' else '1' }.joinToString("")
}