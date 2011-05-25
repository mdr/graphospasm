package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class AddAttributeCommand(val node: Node, val attributeName: AttributeName, attributeValue: AttributeValue) extends Command {

  private var previousDimension: Option[Dimension] = None
  private var addedAnAttribute: Boolean = false
  override def execute() {

    if (node.getAttributes.contains(attributeName, attributeValue))
      return

    node.addAttribute(attributeName, attributeValue)
    addedAnAttribute = true
    previousDimension = AttributeCommandHelper.expandToFitAttributesIfNecessary(node)
  }

  override def undo() {
    if (addedAnAttribute) {
      addedAnAttribute = false
      previousDimension foreach { node.size = _ }
      node.removeAttribute(attributeName)
    }
  }

}

