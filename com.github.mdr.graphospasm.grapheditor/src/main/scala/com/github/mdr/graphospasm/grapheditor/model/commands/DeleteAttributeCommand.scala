package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class DeleteAttributeCommand(node: Node, attributeName: AttributeName) extends AbstractCommand {

  type CommandExecutionData = Option[AttributeValue]

  protected def createCommandExecutionData: Option[AttributeValue] =
    node.getAttributes.toMap.get(attributeName)

  def execute(attributeValue: Option[AttributeValue]) {
    if (attributeValue.isDefined) // It's possible that the attribute has already been removed (e.g. a multiple delete of both name and value)
      node.removeAttribute(attributeName)
  }

  def undo(oldAttributeValue: Option[AttributeValue]) {
    oldAttributeValue foreach { node.addAttribute(attributeName, _) }
  }

}

