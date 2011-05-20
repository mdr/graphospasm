package com.github.mdr.graphospasm.grapheditor.model.commands

import org.eclipse.gef.commands.Command
import com.github.mdr.graphospasm.grapheditor.model.Connection
import com.github.mdr.graphospasm.grapheditor.model.Node

class ReconnectTargetCommand(connection: Connection, newTarget: Node) extends Command {

  private var oldTarget: Node = _

  override def execute() {
    oldTarget = connection.target
    connection.target = newTarget
  }

  override def undo() {
    connection.target = oldTarget
  }
}