package com.github.mdr.graphospasm.grapheditor.part
import com.github.mdr.graphospasm.grapheditor.utils.Utils._
import com.github.mdr.graphospasm.grapheditor.figure._
import com.github.mdr.graphospasm.grapheditor.model.commands._

import org.eclipse.gef.editpolicies.DirectEditPolicy
import org.eclipse.gef.commands.Command
import org.eclipse.gef.editpolicies.DirectEditPolicy
import org.eclipse.gef.requests.DirectEditRequest
import org.eclipse.gef.EditPart

class AttributeValueDirectEditPolicy extends DirectEditPolicy {

  def getDirectEditCommand(edit: DirectEditRequest): Command = {
    val text = edit.getCellEditor.getValue.toString
    val attributeValue = getHost.asInstanceOf[AttributeValueEditPart].getModel
    val newValue = SetAttributeValueCommand.getNewValue(attributeValue.value, text)
    new SetAttributeValueCommand(attributeValue, newValue)
  }

  def showCurrentEditValue(request: DirectEditRequest) {
    //    val value = request.getCellEditor.getValue.toString
    //    getHostFigure.asInstanceOf[AttributeValueFigure].name = value
    //    getHostFigure.getUpdateManager.performUpdate
  }

}
