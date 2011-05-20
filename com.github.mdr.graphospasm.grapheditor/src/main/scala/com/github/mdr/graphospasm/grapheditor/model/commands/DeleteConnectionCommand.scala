package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model.Connection
import org.eclipse.gef.commands.Command
import com.github.mdr.graphospasm.grapheditor.model.Node

class DeleteConnectionCommand(connection: Connection) extends Command {

  override def execute() {
    connection.delete()
  }

  override def undo() {
    connection.undelete()
  }

}
