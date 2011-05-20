package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class RemoveAttributeCommand(node: Node, attributeName: AttributeName) extends Command {

  private var oldAttributeValue: Option[AttributeValue] = None
  private var previousDimension: Option[Dimension] = None

  override def execute() {
    val attributeMap = node.getAttributes.toMap
    oldAttributeValue = attributeMap.get(attributeName)
    if (oldAttributeValue.isDefined) // It's possible that the attribute has already been removed (e.g. a multiple delete of both name and value)
      node.removeAttribute(attributeName)
  }

  override def undo() {
    previousDimension foreach { node.size = _ }
    oldAttributeValue foreach { node.addAttribute(attributeName, _) }
    previousDimension = None
    oldAttributeValue = None
  }

}

