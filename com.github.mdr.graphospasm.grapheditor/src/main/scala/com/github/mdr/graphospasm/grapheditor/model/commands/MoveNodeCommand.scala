package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.grapheditor.model._

import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class MoveNodeCommand(val node: Node, val newBounds: Rectangle) extends Command {

  private var oldBounds: Rectangle = _

  override def execute() {

    val adjustedNewBounds = newBounds.getCopy
    Utils.withFont { font ⇒
      val minHeight = NodeContentsLayouter.layout(node, font).minimumRequiredHeight
      if (adjustedNewBounds.height <= minHeight)
        adjustedNewBounds.height = minHeight
    }
    if (adjustedNewBounds.width < 60)
      adjustedNewBounds.width = 60
    oldBounds = node.bounds
    node.bounds = adjustedNewBounds
  }

  override def undo() {
    node.bounds = oldBounds
  }

}