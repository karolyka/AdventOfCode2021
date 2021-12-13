@Suppress("unused")
class Day12(inputFileName: String) : DayZero(inputFileName) {
    private lateinit var startCave: Cave
    private lateinit var endCave: Cave
    private lateinit var paths: Set<Path>

    override fun preprocess() {
        startCave = Cave("start")
        endCave = Cave("end")
        paths = inputLines.map { Path.build(it) }.toSet()
    }

    override fun task1(): Long {
        findPath(startCave, endCave)
            .also {
                println("Nr. of paths: $it")
                return it.toLong()
            }
    }

    override fun task2(): Long {
        findPath2(startCave, endCave)
            .also {
                println("Nr. of path (double small caves): $it")
                return it.toLong()
            }
    }

    private fun findPath(
        startCave: Cave,
        endCave: Cave,
        visitedSmallCaves: MutableSet<Cave> = mutableSetOf(),
        usedCaves: MutableList<Cave> = mutableListOf()
    ): Int {
        usedCaves.add(startCave)
        var pathCounter = 0

        if (startCave == endCave) {
            pathCounter = 1
        } else {
            if (startCave.isSmall)
                visitedSmallCaves.add(startCave)
            paths.forEach { path ->
                when (startCave) {
                    path.cave1 -> path.cave2
                    path.cave2 -> path.cave1
                    else -> null
                }?.also { target ->
                    if (!visitedSmallCaves.contains(target)) {
                        pathCounter += findPath(target, endCave, visitedSmallCaves, usedCaves)
                    }
                }
            }
            if (startCave.isSmall)
                visitedSmallCaves.remove(startCave)
        }
        usedCaves.removeLast()
        return pathCounter
    }

    private fun findPath2(
        startCave: Cave,
        endCave: Cave,
        visitedSmallCaves: MutableMap<Cave, Int> = mutableMapOf(),
        usedCaves: MutableList<Cave> = mutableListOf()
    ): Int {
        usedCaves.add(startCave)
        var pathCounter = 0

        if (startCave == endCave) {
            pathCounter = 1
        } else {
            if (startCave.isSmall)
                visitedSmallCaves[startCave] =
                    (visitedSmallCaves[startCave] ?: 0) + 1
            paths.forEach { path ->
                when (startCave) {
                    path.cave1 -> path.cave2
                    path.cave2 -> path.cave1
                    else -> null
                }?.also { target ->
                    val maxi = visitedSmallCaves.maxOf { it.value }
                    if ((visitedSmallCaves[target] ?: 0) < (if (target.isStartOrEnd) 1 else 3 - maxi)) {
                        pathCounter += findPath2(target, endCave, visitedSmallCaves, usedCaves)
                    }
                }
            }
            if (startCave.isSmall)
                visitedSmallCaves[startCave] = visitedSmallCaves[startCave]!! - 1
        }
        usedCaves.removeLast()
        return pathCounter
    }

}

data class Cave(val name: String) {
    val isSmall: Boolean
        get() = name.first().isLowerCase()

    val isStartOrEnd: Boolean
        get() = this.name == "start" || this.name == "end"

    override fun toString() = name
}

data class Path(val cave1: Cave, val cave2: Cave) {
    companion object {
        fun build(string: String): Path {
            return string.split('-').let {
                Path(Cave(it.component1()), Cave(it.component2()))
            }
        }
    }
}