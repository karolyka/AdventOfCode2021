@Suppress("unused")
class Day08(inputFileName: String) : DayZero(inputFileName) {
    private lateinit var codes: List<Codes>

    override fun preprocess() {
        codes = inputLines.map { line ->
            line.split(" | ").let {
                Codes(it.component1().splitBy(), it.component2().splitBy())
            }
        }
    }

    override fun task1(): Long {
        codes.sumOf { line -> line.digits.count { it.length in listOf(2, 3, 4, 7) } }.also {
            println("Unique numbers: $it")
            return it.toLong()
        }
    }

    override fun task2(): Long {
        codes
            .map { it.codes.decode() to it }
            .sumOf { it.second.digits.map { digit -> it.first[digit]!! }.joinToString("").toInt().also { println(it) } }
            .also {
                println("Sum: $it")
                return it.toLong()
            }
    }

    private fun String.splitBy(delimiter: Char = ' ') =
        split(delimiter).map { it.trim().toSortedSet().joinToString("") }.toList()
}

private fun List<String>.decode(): MutableMap<String, Int> {
    val map = mutableMapOf<String, Int>()
    listOf(Pair(1, 2), Pair(4, 4), Pair(7, 3), Pair(8, 7)).forEach { pair ->
        first { it.length == pair.second }.also { map[it] = pair.first }
    }
    first { it.length == 5 && map.getByValue(1).key.all { c -> it.contains(c) } }.also {
        map[it] = 3
    }
    first { it.length == 6 && map.getByValue(4).key.all { c -> it.contains(c) } }.also {
        map[it] = 9
    }
    first { it.length == 6 && map[it] == null && (map.getByValue(1).key.toSet() - it.toSet()).size == 1 }.also {
        map[it] = 6
    }
    first { it.length == 5 && map[it] == null && (map.getByValue(9).key.toSet() - it.toSet()).size == 1 }.also {
        map[it] = 5
    }
    first { it.length == 6 && map[it] == null }.also {
        map[it] = 0
    }
    first { it.length == 5 && map[it] == null }.also {
        map[it] = 2
    }
    return map
}

private fun <K, V> MutableMap<K, V>.getByValue(value: V): Map.Entry<K, V> = entries.first { it.value == value }

data class Codes(val codes: List<String>, val digits: List<String>)