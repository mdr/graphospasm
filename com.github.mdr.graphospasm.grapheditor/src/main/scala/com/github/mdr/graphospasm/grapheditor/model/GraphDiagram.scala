package com.github.mdr.graphospasm.grapheditor.model

import com.github.mdr.graphospasm.grapheditor.utils.Utils
import org.eclipse.draw2d.FigureUtilities
import org.eclipse.swt.graphics.GC
import com.github.mdr.graphospasm.core.graph._
import org.eclipse.draw2d.geometry._
import org.eclipse.draw2d._
import org.eclipse.swt.widgets.Shell
import scala.math.max

object GraphDiagram {

  def create(graph: Graph): GraphDiagram = {
    Utils.withFont { font ⇒
      val diagram = new GraphDiagram
      var vertexToNode = Map[Vertex, Node]()

      for (vertex ← graph.vertices) {
        val node = new Node(vertex.name)
        for ((name, value) ← vertex.attributes)
          node.addAttribute(name, value)
        val attributes = vertex.attributes map { case (Name(simpleName, _), value) ⇒ (simpleName, value) }
        node.attributes = attributes

        val NodeContentsLayoutInfo(_, _, _, minimumRequiredWidth, minimumRequiredHeight) =
          NodeContentsLayouter.layout(node, font)

        node.bounds = new Rectangle(150, 150, minimumRequiredWidth, minimumRequiredHeight)
        diagram.add(node)
        vertexToNode = vertexToNode + (vertex -> node)
      }
      for (edge ← graph.edges)
        Connection.connect(vertexToNode(edge.source), vertexToNode(edge.target))

      diagram
    }
  }
}

class GraphDiagram extends Observable {

  private var nodes_ : List[Node] = Nil

  def nodes = nodes_

  def add(node: Node) {
    insert(node, nodes_.length)
  }

  def insert(node: Node, index: Int) {
    nodes_ = nodes_.patch(index, List(node), 0)
    node.setDiagram(this)
    fireEvent(NodeInserted(node, index))
  }

  def remove(node: Node) {
    val index = indexOf(node)
    nodes_ = nodes_ filterNot { _ == node }
    node.unsetDiagram()
    fireEvent(NodeRemoved(node, index))
  }

  def indexOf(thing: Node) = nodes_ indexOf thing

}

