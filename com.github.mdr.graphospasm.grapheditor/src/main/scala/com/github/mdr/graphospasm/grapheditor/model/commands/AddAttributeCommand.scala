package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class AddAttributeCommand(node: Node) extends Command {

  private var attributeName: AttributeName = _
  private var previousDimension: Option[Dimension] = None

  override def execute() {
    // TODO: Ensure unique name

    val existingAttributes = node.getAttributes.map(_._1.name.simpleName).toSet

    var candidateName = "name"
    var i = 0
    while (existingAttributes contains candidateName) {
      i += 1
      candidateName = "name" + i
    }

    val (newName, _) = node.addAttribute(Name(candidateName), "value")

    Utils.withFont { font ⇒
      val nodeContentsLayoutInfo = NodeContentsLayouter.layout(node, font)
      if (node.height < nodeContentsLayoutInfo.minimumRequiredHeight) {
        previousDimension = Some(node.size)
        val newSize = node.size.getCopy
        newSize.height = nodeContentsLayoutInfo.minimumRequiredHeight
        node.size = newSize
      }
    }
    attributeName = newName

  }

  override def undo() {
    previousDimension foreach { node.size = _ }
    node.removeAttribute(attributeName)
  }

}

