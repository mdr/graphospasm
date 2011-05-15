package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model.NodeName
import com.github.mdr.graphospasm.grapheditor.figure.NodeNameFigure
import com.github.mdr.graphospasm.grapheditor.model.commands._

import org.eclipse.gef.editpolicies.DirectEditPolicy
import org.eclipse.gef.commands.Command
import org.eclipse.gef.editpolicies.DirectEditPolicy
import org.eclipse.gef.requests.DirectEditRequest
import org.eclipse.gef.EditPart

class RenameDirectEditPolicy extends DirectEditPolicy {

  def getDirectEditCommand(edit: DirectEditRequest): Command = {
    val name = edit.getCellEditor.getValue.toString
    val editPart = getHost.asInstanceOf[NodeNameEditPart]
    val node = editPart.getModel
    new RenameCommand(node, name)
  }

  def showCurrentEditValue(request: DirectEditRequest) {
    val value = request.getCellEditor.getValue.toString
    getHostFigure.asInstanceOf[NodeNameFigure].name = value
    getHostFigure.getUpdateManager.performUpdate
  }

}
