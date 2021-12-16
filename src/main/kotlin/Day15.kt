import baeldung.Dijkstra
import baeldung.Graph
import baeldung.Node

@Suppress("unused")
class Day15(inputFileName: String) : DayZero(inputFileName) {

    private lateinit var points: MutableList<List<Int>>
    private lateinit var targetNode: Node
    private lateinit var startNode: Node
    private lateinit var startPosition: Pair<Int, Int>
    private lateinit var targetPosition: Pair<Int, Int>

    private lateinit var nodes: MutableMap<String, Node>
    private lateinit var graph: Graph

    private fun buildGraph(multiply: Int) {
        points = inputLines.map { line ->
            (0 until multiply).flatMap { i ->
                line.map { it.digitToInt().plus(i).over() }
            }
        }.toMutableList()
        val multiplyEntries = mutableListOf<List<Int>>()
        (1 until multiply).forEach { i ->
            points.forEach { row ->
                multiplyEntries.add(row.map { (it + i).over() }.toList())
            }
        }
        points.addAll(multiplyEntries)

        val rows = points.size
        val columns = points.first().size
        graph = Graph()
        nodes = mutableMapOf()
        startPosition = 0 to 0
        targetPosition = points.size - 1 to columns - 1
        startNode = getNode(startPosition.toString())
        targetNode = getNode(targetPosition.toString())
        (points.indices).forEach { row ->
            createEdge(columns, row, rows)
        }
        nodes.forEach { graph.addNode(it.value) }
    }

    private fun createEdge(columns: Int, row: Int, rows: Int) {
        (0 until columns).forEach { column ->
            val fromNode = getNode(row to column)
            if (column + 1 < columns) {
                fromNode.addDestination(getNode(row to column + 1), points[row][column + 1])
            }
            if (row + 1 < rows) {
                fromNode.addDestination(getNode(row + 1 to column), points[row + 1][column])
            }
        }
    }

    private fun getNode(node: Pair<Int, Int>) = getNode(node.toString())
    private fun getNode(nodeName: String) = nodes[nodeName] ?: Node(nodeName).also { nodes[nodeName] = it }

    override fun task1(): Long {
        buildGraph(1)
        Dijkstra.calculateShortestPathFromSource(graph, startNode).nodes.first { it == targetNode }.distance.toLong()
            .also {
                println("Lowest risk: $it")
                return it
            }
    }

    override fun task2(): Long {
        buildGraph(5)
        Dijkstra.calculateShortestPathFromSource(graph, startNode).nodes.first { it == targetNode }.distance.toLong()
            .also {
                println("Lowest risk (5x): $it")
                return it
            }
    }

}

private fun Int.over() = if (this > 9) (this % 10) + 1 else this
