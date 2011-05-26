package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class DeleteNodeCommand(node: Node) extends AbstractCommand {

  case class DeleteNodeData(priorIndex: Int, connections: List[Connection], diagram: GraphDiagram)

  type CommandExecutionData = DeleteNodeData

  def createCommandExecutionData = DeleteNodeData(node.diagram indexOf node, node.allConnections.distinct, node.diagram)

  def execute(data: DeleteNodeData) {
    data.connections.foreach { _.delete() }
    data.diagram.remove(node)
  }

  def undo(data: DeleteNodeData) {
    data.diagram.insert(node, data.priorIndex)
    data.connections.reverse.foreach { _.undelete() }
  }

}
