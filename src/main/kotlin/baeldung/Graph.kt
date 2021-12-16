package baeldung

class Graph {
    val nodes: MutableSet<Node> = HashSet()
    fun addNode(nodeA: Node) {
        nodes.add(nodeA)
    }
}