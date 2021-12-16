package baeldung

import java.util.*

object Dijkstra {
    fun calculateShortestPathFromSource(graph: Graph, source: Node): Graph {
        source.distance = 0
        val settledNodes: MutableSet<Node?> = HashSet()
        val unsettledNodes: MutableSet<Node?> = HashSet()
        unsettledNodes.add(source)
        while (unsettledNodes.size != 0) {
            val currentNode = getLowestDistanceNode(unsettledNodes)
            unsettledNodes.remove(currentNode)
            for ((adjacentNode, edgeWeigh) in currentNode!!.adjacentNodes) {
                if (!settledNodes.contains(adjacentNode)) {
                    CalculateMinimumDistance(adjacentNode, edgeWeigh, currentNode)
                    unsettledNodes.add(adjacentNode)
                }
            }
            settledNodes.add(currentNode)
        }
        return graph
    }

    private fun CalculateMinimumDistance(evaluationNode: Node, edgeWeigh: Int, sourceNode: Node?) {
        val sourceDistance = sourceNode!!.distance
        if (sourceDistance + edgeWeigh < evaluationNode.distance) {
            evaluationNode.distance = sourceDistance + edgeWeigh
            val shortestPath = LinkedList(sourceNode.getShortestPath())
            shortestPath.add(sourceNode)
            evaluationNode.setShortestPath(shortestPath)
        }
    }

    private fun getLowestDistanceNode(unsettledNodes: Set<Node?>): Node? {
        var lowestDistanceNode: Node? = null
        var lowestDistance = Int.MAX_VALUE
        for (node in unsettledNodes) {
            val nodeDistance = node!!.distance
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance
                lowestDistanceNode = node
            }
        }
        return lowestDistanceNode
    }
}