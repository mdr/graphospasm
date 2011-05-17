package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class RemoveAttributeCommand(node: Node, attributeName: AttributeName) extends Command {

  private var attributeValue: AttributeValue = _
  private var previousDimension: Option[Dimension] = None

  override def execute() {
    val map = node.getAttributes.toMap
    attributeValue = map(attributeName)
    node.removeAttribute(attributeName)

    //    Utils.withFont { font ⇒
    //      val nodeContentsLayoutInfo = NodeContentsLayouter.layout(node, font)
    //      if (nodeContentsLayoutInfo.minimumRequiredHeight != node.height) {
    //        previousDimension = Some(node.size)
    //        val newSize = node.size.getCopy
    //        newSize.height = nodeContentsLayoutInfo.minimumRequiredHeight
    //        node.size = newSize
    //      }
    //    }

  }

  override def undo() {
    previousDimension foreach { node.size = _ }
    node.addAttribute(attributeName, attributeValue)
  }

}

