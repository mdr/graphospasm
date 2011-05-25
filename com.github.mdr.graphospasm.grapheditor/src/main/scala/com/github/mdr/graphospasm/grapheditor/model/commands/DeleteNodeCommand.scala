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
  private var oldConnections: List[Connection] = Nil
  private var outgoingConnectionTargets: List[Node] = Nil
  private var incomingConnectionSources: List[Node] = Nil

  override def execute() {
    oldConnections = node.allConnections.distinct
    oldConnections.foreach { _.delete() }
    priorIndex = diagram indexOf node
    diagram.remove(node)
  }

  override def undo() {
    diagram.insert(node, priorIndex)
    oldConnections.foreach { _.undelete() }
  }

}
