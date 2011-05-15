package com.github.mdr.graphospasm.grapheditor.model.commands
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.gef.commands.Command

class CreateConnectionCommand(source: Node) extends Command {

  private var connection: Connection = _

  private var targetOpt: Option[Node] = None

  def setTarget(node: Node) { targetOpt = Some(node) }

  override def canExecute = targetOpt.isDefined

  override def execute() {
    connection = Connection.connect(source, targetOpt.get)
  }

  override def undo() {
    connection.delete()
  }

}