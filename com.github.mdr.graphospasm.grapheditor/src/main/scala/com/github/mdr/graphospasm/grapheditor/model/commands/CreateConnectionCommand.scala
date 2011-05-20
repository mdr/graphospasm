package com.github.mdr.graphospasm.grapheditor.model.commands
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.gef.commands.Command

class CreateConnectionCommand(source: Node) extends Command {

  private var connectionOpt: Option[Connection] = None

  private var targetOpt: Option[Node] = None

  def setTarget(node: Node) { targetOpt = Some(node) }

  override def canExecute = targetOpt.isDefined

  override def execute() {
    connectionOpt match {
      case None ⇒
        connectionOpt = Some(Connection.connect(source, targetOpt.get))
      case Some(connection) ⇒
        connection.undelete()
    }
  }

  override def undo() {
    connectionOpt.get.delete()
  }

}