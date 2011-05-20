package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model.Connection
import com.github.mdr.graphospasm.grapheditor.model.Node
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class DeleteNodeCommand(node: Node) extends Command {

  private final val diagram = node.diagram
  private var priorIndex: Int = _
  private var outgoingConnectionTargets: List[Node] = Nil
  private var incomingConnectionSources: List[Node] = Nil

  override def execute() {
    priorIndex = diagram indexOf node
    diagram.remove(node)

    outgoingConnectionTargets = node.sourceConnections.map(_.target)
    node.sourceConnections foreach { _.delete() }
    incomingConnectionSources = node.targetConnections.map(_.source)
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
