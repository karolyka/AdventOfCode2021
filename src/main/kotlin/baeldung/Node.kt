package baeldung

import java.util.*

data class Node(var name: String) {
    private var shortestPath = LinkedList<Node>()
    var distance = Int.MAX_VALUE
    var adjacentNodes: MutableMap<Node, Int> = HashMap()
    fun addDestination(destination: Node, distance: Int) {
        adjacentNodes[destination] = distance
    }

    fun getShortestPath(): List<Node> {
        return shortestPath
    }

    fun setShortestPath(shortestPath: LinkedList<Node>) {
        this.shortestPath = shortestPath
    }
}