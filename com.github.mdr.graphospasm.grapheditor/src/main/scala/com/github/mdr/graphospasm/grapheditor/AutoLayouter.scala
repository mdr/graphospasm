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

object AutoLayouter {

  def autolayoutDiagram(diagram: GraphDiagram) {

    val directedGraphLayout = new DirectedGraphLayout
    val graph = new DirectedGraph

    var thingToNodeMap: Map[Node, Draw2DNode] = Map()
    var connections: Set[Connection] = Set()
    for (node ← diagram.nodes) {
      val draw2dNode = new Draw2DNode(node)
      draw2dNode.width = node.width
      draw2dNode.height = node.height
      draw2dNode.setPadding(new Insets(8, 4, 8, 4))
      thingToNodeMap = thingToNodeMap + (node -> draw2dNode)
      graph.nodes.asInstanceOf[ArrayList[Draw2DNode]].add(draw2dNode)
      connections = connections ++ node.allConnections
    }
    for {
      connection ← connections
      source = connection.source
      target = connection.target
    } {
      val sourceNode = thingToNodeMap(source)
      val targetNode = thingToNodeMap(target)
      val edge = new Edge(connection, sourceNode, targetNode)
      graph.edges.asInstanceOf[ArrayList[Edge]].add(edge)
    }

    directedGraphLayout.visit(graph)
    for ((thing, node) ← thingToNodeMap) {
      val bounds = new Rectangle(node.x + 2, node.y + 2, node.width, thing.height)
      thing.bounds = bounds
    }
  }

}