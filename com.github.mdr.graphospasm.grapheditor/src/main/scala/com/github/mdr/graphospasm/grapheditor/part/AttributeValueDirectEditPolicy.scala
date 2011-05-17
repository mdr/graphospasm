package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model.NodeName
import com.github.mdr.graphospasm.grapheditor.figure._
import com.github.mdr.graphospasm.grapheditor.model.commands._

import org.eclipse.gef.editpolicies.DirectEditPolicy
import org.eclipse.gef.commands.Command
import org.eclipse.gef.editpolicies.DirectEditPolicy
import org.eclipse.gef.requests.DirectEditRequest
import org.eclipse.gef.EditPart

class AttributeValueDirectEditPolicy extends DirectEditPolicy {

  def getDirectEditCommand(edit: DirectEditRequest): Command = {
    val name = edit.getCellEditor.getValue.toString
    val editPart = getHost.asInstanceOf[AttributeValueEditPart]
    val attributeValue = editPart.getModel
    new SetAttributeValueCommand(attributeValue, name)
  }

  def showCurrentEditValue(request: DirectEditRequest) {
    val value = request.getCellEditor.getValue.toString
    getHostFigure.asInstanceOf[AttributeValueFigure].name = value
    getHostFigure.getUpdateManager.performUpdate
  }

}
