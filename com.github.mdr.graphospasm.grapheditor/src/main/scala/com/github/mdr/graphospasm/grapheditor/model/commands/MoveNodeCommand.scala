package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model._

import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

class MoveNodeCommand(val thing: Node, val newLocation: Rectangle) extends Command {

  private var oldBounds: Rectangle = _

  override def execute() {
    oldBounds = thing.bounds
    thing.bounds = newLocation
  }

  override def undo() {
    thing.bounds = oldBounds
  }

}