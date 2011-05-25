package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.grapheditor.model._

import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class MoveNodeCommand(val node: Node, val newBounds: Rectangle) extends AbstractCommand {

  case class MoveNodeData(oldBounds: Rectangle, newBounds: Rectangle)

  type CommandExecutionData = MoveNodeData

  def createCommandExecutionData = {
    val adjustedNewBounds = newBounds.getCopy
    Utils.withFont { font ⇒
      val minHeight = NodeContentsLayouter.layout(node, font).minimumRequiredHeight
      if (adjustedNewBounds.height <= minHeight)
        adjustedNewBounds.height = minHeight
    }
    if (adjustedNewBounds.width < 60)
      adjustedNewBounds.width = 60
    MoveNodeData(node.bounds, adjustedNewBounds)
  }

  def execute(data: MoveNodeData) {
    node.bounds = data.newBounds
  }

  def undo(data: MoveNodeData) {
    node.bounds = data.oldBounds
  }

}