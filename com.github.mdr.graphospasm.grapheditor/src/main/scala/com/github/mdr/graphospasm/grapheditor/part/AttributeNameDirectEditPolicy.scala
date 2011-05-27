package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.figure._
import com.github.mdr.graphospasm.grapheditor.model.commands._

import org.eclipse.gef.editpolicies.DirectEditPolicy
import org.eclipse.gef.commands.Command
import org.eclipse.gef.editpolicies.DirectEditPolicy
import org.eclipse.gef.requests.DirectEditRequest
import org.eclipse.gef.EditPart

class AttributeNameDirectEditPolicy extends DirectEditPolicy {

  override def getHost = super.getHost.asInstanceOf[AttributeNameEditPart]

  def getDirectEditCommand(edit: DirectEditRequest): Command =
    if (edit.getCellEditor.isValueValid) {
      val name = edit.getCellEditor.getValue.toString
      new RenameAttributeNameCommand(getHost.getModel, name)
    } else
      null

  def showCurrentEditValue(request: DirectEditRequest) {
    if (request.getCellEditor.isValueValid) {
      val value = request.getCellEditor.getValue.toString
      getHostFigure.asInstanceOf[AbstractNameFigure].name = value
      getHostFigure.getUpdateManager.performUpdate
    }
  }

}
