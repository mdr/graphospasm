package com.github.mdr.graphospasm.grapheditor.model.commands
import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.core.graph._
import org.eclipse.gef.commands.Command

class CreateConnectionCommand(source: Node, labelOpt: Option[Name] = None) extends AbstractCommand {

  private var targetOpt: Option[Node] = None

  def setTarget(node: Node) { targetOpt = Some(node) }

  override def canExecute = targetOpt.isDefined

  type CommandExecutionData = Connection

  protected def createCommandExecutionData = Connection.create(source, targetOpt.get, labelOpt)

  def connection = dataOpt.get

  protected def execute(connection: Connection) {
    connection.undelete()
  }

  protected def undo(connection: Connection) {
    connection.delete()
  }

}