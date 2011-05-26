package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.core.graph._
import org.eclipse.gef.commands.Command
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command;

class SetEdgeLabelCommand(val connection: Connection, newSimpleNameOpt: Option[String]) extends AbstractCommand {

  case class SetEdgeLabelData(oldNameOpt: Option[Name], newNameOpt: Option[Name])

  type CommandExecutionData = SetEdgeLabelData

  def createCommandExecutionData: SetEdgeLabelData = {
    val oldNameOpt = connection.nameOpt
    val newNameOpt = newSimpleNameOpt.map { newSimpleName ⇒
      oldNameOpt.getOrElse(Name("")).copy(simpleName = newSimpleName)
    }
    SetEdgeLabelData(oldNameOpt, newNameOpt)
  }

  def execute(data: SetEdgeLabelData) { connection.nameOpt = data.newNameOpt }

  def undo(data: SetEdgeLabelData) { connection.nameOpt = data.oldNameOpt }

}