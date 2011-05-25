package com.github.mdr.graphospasm.grapheditor.model.commands

import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command
import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.utils.Utils

class CloneAttributesCommand(node: Node, originalAttributes: List[(AttributeName, AttributeValue)]) extends Command {

  private var addedChildren: List[Node] = Nil
  private var previousDimension: Option[Dimension] = None

  private lazy val clonedAttributes = originalAttributes.map { case (name, value) ⇒ (name.copy, value.copy) }

  override def execute() {
    for ((name, value) ← clonedAttributes)
      node.addAttribute(name, value)

    Utils.withFont { font ⇒
      val nodeContentsLayoutInfo = NodeContentsLayouter.layout(node, font)
      if (node.height < nodeContentsLayoutInfo.minimumRequiredHeight) {
        previousDimension = Some(node.size)
        val newSize = node.size.getCopy
        newSize.height = nodeContentsLayoutInfo.minimumRequiredHeight
        node.size = newSize
      }
    }
  }

  override def undo() {
    previousDimension foreach { node.size = _ }
    for ((name, value) ← clonedAttributes.reverse)
      node.removeAttribute(name)
  }

}