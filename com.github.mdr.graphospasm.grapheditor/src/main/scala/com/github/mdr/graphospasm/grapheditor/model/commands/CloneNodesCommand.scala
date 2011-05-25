package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.core.graph._
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command
import com.github.mdr.graphospasm.grapheditor.model._

case class CloneNodesData(nodes: List[Node], connections: List[Connection])

class CloneNodesCommand(diagram: GraphDiagram, originalNodesAndCloneLocations: Map[Node, Rectangle]) extends AbstractCommand {

  type CommandExecutionData = CloneNodesData

  def execute(data: CloneNodesData) {
    data.nodes.foreach(diagram.add)
    data.connections.foreach { _.undelete() }
  }

  def undo(data: CloneNodesData) {
    data.connections.reverse.foreach { _.delete() }
    data.nodes.reverse.foreach(diagram.remove)
  }

  protected def createCommandExecutionData: CloneNodesData = {
    var originalToClone: Map[Node, Node] = Map()
    val nodes = originalNodesAndCloneLocations.toList.map {
      case (node, newLocation) ⇒
        val clonedNode = node.copy
        clonedNode.bounds = newLocation
        originalToClone += node -> clonedNode
        clonedNode
    }

    val connections = for {
      (originalNode, _) ← originalNodesAndCloneLocations.toList
      connection ← originalNode.sourceConnections // Just source connections to avoid double counting
      clonedSource ← originalToClone.get(connection.source)
      clonedTarget ← originalToClone.get(connection.target)
    } yield Connection.create(clonedSource, clonedTarget, connection.nameOpt)
    CloneNodesData(nodes, connections)
  }

}
