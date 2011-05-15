package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model.Node
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class DeleteNodeCommand(node: Node) extends Command {

  private val diagram = node.diagram
  private var priorIndex: Int = _
  private val outgoingConnectionTargets = node.sourceConnections map { _.target }
  //  private val incomingConnectionSources = node.targetConnections flatMap { _.sourceOpt }

  override def execute() {
    //    priorIndex = priorParent indexOf node
    //    priorParent.removeChild(node)
    //    node.sourceConnections foreach { _.delete() }
    //    node.targetConnections foreach { _.delete() }
  }

  override def undo() {
    //    priorParent.insertChild(node, priorIndex)
    //    for (target ← outgoingConnectionTargets)
    //      new Connection(Some(node), Some(target))
    //    for (source ← incomingConnectionSources)
    //      new Connection(Some(source), Some(node))
  }

}
