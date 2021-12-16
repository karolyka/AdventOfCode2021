@Suppress("unused")
class Day14(inputFileName: String) : DayZero(inputFileName) {

    private lateinit var doubleRules: List<DoubleRule>
    private lateinit var rules: List<Rule>
    private lateinit var startPoly: String

    override fun preprocess() {
        startPoly = inputLines.first()
        rules = inputLines.drop(2).map { line ->
            line.split(" -> ").let { Rule(it.component1(), it.component2()) }
        }
        doubleRules = rules.map { DoubleRule.from(it) }
    }

    override fun task1(): Long {
        simulatePolymer(10)
            .also {
                println("10 steps, Max - Min: $it")
                return it
            }
    }

    override fun task2(): Long {
        simulatePolymer(40)
            .also {
                println("40 steps, Max - Min: $it")
                return it
            }
    }

    private fun countPolymerElements(list: List<PolymerWindow>): Map<Char, Long> {
        val firstLetter = startPoly.first()
        val map = mutableMapOf<Char, Long>()
        list.forEach { polymerWindow ->
            polymerWindow.id[1].also { secondLetter ->
                map[secondLetter] = (map[secondLetter] ?: 0) + polymerWindow.count
            }
        }
        map[firstLetter] = (map[firstLetter] ?: 0) + 1
        return map.toMap()
    }

    private fun simulatePolymer(gens: Int): Long {
        var currentWindow = startPoly.windowed(2).map { PolymerWindow(it, 1) }
        repeat(gens) { currentWindow = performInsertion(currentWindow) }
        return countPolymerElements(currentWindow).let { elements -> elements.maxOf { it.value } - elements.minOf { it.value } }
    }

    private fun performInsertion(windows: List<PolymerWindow>): List<PolymerWindow> {
        val newPolyWindows = windows.flatMap { window ->
            doubleRules.find { it.from == window.id }!!
                .tos
                .map { newId -> PolymerWindow(newId, window.count) }
        }
        return newPolyWindows.map { it.id }.distinct().map { uid ->
            PolymerWindow(uid, newPolyWindows.filter { it.id == uid }.sumOf { it.count })
        }
    }

}

data class Rule(val from: String, val to: String)

data class DoubleRule(val from: String, val tos: List<String>) {
    companion object {
        fun from(r: Rule): DoubleRule {
            return DoubleRule(r.from, listOf("${r.from[0]}${r.to}", "${r.to}${r.from[1]}"))
        }
    }
}

data class PolymerWindow(val id: String, val count: Long)
