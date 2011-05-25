package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model.commands._
import org.eclipse.gef.commands.Command
import org.eclipse.gef.editpolicies.ComponentEditPolicy
import org.eclipse.gef.requests.GroupRequest
import org.eclipse.gef.Request

class AttributeNameComponentEditPolicy extends ComponentEditPolicy {

  override def getHost = super.getHost.asInstanceOf[AttributeNameEditPart]

  override protected def createDeleteCommand(deleteRequest: GroupRequest) =
    new DeleteAttributeCommand(getHost.getParent.getModel, getHost.getModel)

}