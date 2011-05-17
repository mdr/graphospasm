package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.core.graph._
import org.eclipse.gef.commands.Command
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command;

class SetAttributeValueCommand(val attributeValue: AttributeValue, val newValue: String) extends Command {

  private var oldValue: String = _

  override def execute() {
    oldValue = attributeValue.presentationString
    attributeValue.value = newValue
  }

  override def undo() {
    attributeValue.value = oldValue
  }

}