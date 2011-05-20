package com.github.mdr.graphospasm.grapheditor

import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.core.graph.xml._

import com.github.mdr.graphospasm.grapheditor.part._
import com.github.mdr.graphospasm.grapheditor.model.{ Node, _ }

import org.eclipse.draw2d.FigureUtilities
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.graphics.GC
import java.util.ArrayList
import org.eclipse.draw2d.graph.DirectedGraphLayout
import org.eclipse.draw2d.graph.{ Node ⇒ Draw2DNode, Edge, DirectedGraph }
import org.eclipse.draw2d.geometry._
import org.eclipse.gef.ui.parts._
import org.eclipse.gef._
import org.eclipse.core.runtime._
import scala.xml._
import com.github.mdr.graphospasm.grapheditor.utils.Utils

object AutoLayouter {

  def autolayoutDiagram(diagram: GraphDiagram) = {
    var oldLocations: Map[Node, Rectangle] = Map()

    for (node ← diagram.nodes) {
      oldLocations += node -> node.bounds
      Utils.withFont { font ⇒
        val height = NodeContentsLayouter.layout(node, font).minimumRequiredHeight
        val width = NodeContentsLayouter.preferredWidth(node, font)
        node.size = new Dimension(width, height)
      }
    }

    val directedGraphLayout = new DirectedGraphLayout
    val graph = new DirectedGraph

    var nodeToDraw2dNodeMap: Map[Node, Draw2DNode] = Map()
    var connections: Set[Connection] = Set()
    for (node ← diagram.nodes) {
      val draw2dNode = new Draw2DNode(node)
      draw2dNode.width = node.width
      draw2dNode.height = node.height
      draw2dNode.setPadding(new Insets(8, 4, 8, 4))
      nodeToDraw2dNodeMap += node -> draw2dNode
      graph.nodes.asInstanceOf[ArrayList[Draw2DNode]].add(draw2dNode)
      connections = connections ++ node.allConnections
    }
    for {
      connection ← connections
      source = connection.source
      target = connection.target
    } {
      val sourceNode = nodeToDraw2dNodeMap(source)
      val targetNode = nodeToDraw2dNodeMap(target)
      val edge = new Edge(connection, sourceNode, targetNode)
      graph.edges.asInstanceOf[ArrayList[Edge]].add(edge)
    }

    directedGraphLayout.visit(graph)
    for ((node, draw2DNode) ← nodeToDraw2dNodeMap) {
      val bounds = new Rectangle(draw2DNode.x + 2, draw2DNode.y + 2, node.width, node.height)
      node.bounds = bounds
    }
    oldLocations
  }

}