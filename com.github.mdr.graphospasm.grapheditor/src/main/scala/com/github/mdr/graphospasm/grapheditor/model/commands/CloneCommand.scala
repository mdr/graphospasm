package com.github.mdr.graphospasm.grapheditor.model.commands

import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command
import com.github.mdr.graphospasm.grapheditor.model._

class CloneCommand(diagram: GraphDiagram, originalNodesAndCloneLocations: Map[Node, Rectangle]) extends Command {

  private var addedChildren: List[Node] = Nil

  override def execute() {
    var originalToClone: Map[Node, Node] = Map()
    originalNodesAndCloneLocations foreach {
      case (node, newLocation) ⇒
        val clonedNode = node.copy
        clonedNode.bounds = newLocation
        diagram.add(clonedNode)
        addedChildren ::= clonedNode
        originalToClone += node -> clonedNode
    }

    for {
      (originalNode, _) ← originalNodesAndCloneLocations
      connection ← originalNode.sourceConnections // Just source connections to avoid double counting
      clonedSource ← originalToClone.get(connection.source)
      clonedTarget ← originalToClone.get(connection.target)
    } Connection.connect(clonedSource, clonedTarget)
  }

  override def undo() {
    addedChildren.foreach(diagram.remove)
  }

}
