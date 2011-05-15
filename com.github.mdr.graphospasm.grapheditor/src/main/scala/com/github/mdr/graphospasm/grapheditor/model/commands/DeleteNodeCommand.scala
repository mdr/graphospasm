package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model.Connection
import com.github.mdr.graphospasm.grapheditor.model.Node
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class DeleteNodeCommand(node: Node) extends Command {

  private val diagram = node.diagram
  private var priorIndex: Int = _
  private val outgoingConnectionTargets = node.sourceConnections.map(_.target)
  private val incomingConnectionSources = node.targetConnections.map(_.source)

  override def execute() {
    priorIndex = diagram indexOf node
    diagram.remove(node)
    node.sourceConnections foreach { _.delete() }
    node.targetConnections foreach { _.delete() }
  }

  // TODO: Want to preserve connection order
  override def undo() {
    diagram.insert(node, priorIndex)
    for (target ← outgoingConnectionTargets)
      Connection.connect(node, target)
    for (source ← incomingConnectionSources)
      Connection.connect(source, node)
  }

}
