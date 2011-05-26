package com.github.mdr.graphospasm.grapheditor.model.commands
import com.github.mdr.graphospasm.grapheditor.utils.Utils._

import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.core.graph._
import org.eclipse.gef.commands.Command
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command;
import java.lang.{ Integer ⇒ JInteger }

object SetAttributeValueCommand {

  def getNewValue(value: AnyRef, text: String): AnyRef =
    value match {
      case _: JInteger ⇒
        parseInt(text) match {
          case Some(n) ⇒ n.asInstanceOf[AnyRef]
          case None    ⇒ text
        }
      case _ ⇒ text
    }

}

class SetAttributeValueCommand(val attributeValue: AttributeValue, val newValue: AnyRef) extends Command {

  private var oldValue: AnyRef = _

  override def execute() {
    oldValue = attributeValue.value
    attributeValue.value = newValue
  }

  override def undo() {
    attributeValue.value = oldValue
  }

}