package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class AddAttributeCommand(node: Node) extends Command {

  private var attributeName: AttributeName = _

  override def execute() {
    // TODO: Ensure unique name
    val (newName, _) = node.addAttribute(Name("name"), "value")
    attributeName = newName
  }

  override def undo() {
    node.removeAttribute(attributeName)
  }

}

