package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model.commands.DeleteNodeCommand
import org.eclipse.gef.commands.Command
import org.eclipse.gef.editpolicies.ComponentEditPolicy
import org.eclipse.gef.requests.GroupRequest
import org.eclipse.gef.Request

class NodeComponentEditPolicy extends ComponentEditPolicy {

  override def getHost = super.getHost.asInstanceOf[NodeEditPart]

  override protected def createDeleteCommand(deleteRequest: GroupRequest) = new DeleteNodeCommand(getHost.getModel)

}