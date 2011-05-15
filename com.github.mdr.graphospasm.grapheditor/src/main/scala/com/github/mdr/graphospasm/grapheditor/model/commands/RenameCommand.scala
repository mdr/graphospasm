package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model.NodeName
import com.github.mdr.graphospasm.core.graph._
import org.eclipse.gef.commands.Command
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command;

class RenameCommand(val nodeName: NodeName, val newName: String) extends Command {

  private var oldName: Name = _

  override def execute() {
    oldName = nodeName.name
    nodeName.name = oldName.copy(simpleName = newName)
  }

  override def undo() {
    nodeName.name = oldName
  }

}