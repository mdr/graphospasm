package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.core.graph._
import org.eclipse.gef.commands.Command
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command;

class SetEdgeLabelCommand(connection: Connection, newSimpleNameOpt: Option[String]) extends Command {

  private var oldNameOpt: Option[Name] = None

  override def execute() {
    oldNameOpt = connection.nameOpt
    val existingNameOpt = connection.nameOpt
    connection.nameOpt = newSimpleNameOpt.map { newSimpleName ⇒
      existingNameOpt match {
        case None               ⇒ Name(newSimpleName)
        case Some(existingName) ⇒ existingName.copy(simpleName = newSimpleName)
      }
    }
  }

  override def undo() {
    connection.nameOpt = oldNameOpt
  }

}