package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model.commands._
import org.eclipse.gef.commands.Command
import org.eclipse.gef.editpolicies.ComponentEditPolicy
import org.eclipse.gef.requests.GroupRequest
import org.eclipse.gef.Request

class AttributeValueComponentEditPolicy extends ComponentEditPolicy {

  override def getHost = super.getHost.asInstanceOf[AttributeValueEditPart]

  override def getOrphanCommand() = {
    super.getOrphanCommand()
  }

  override protected def createDeleteCommand(deleteRequest: GroupRequest): RemoveAttributeCommand = {
    val node = getHost.getParent.getModel
    node.getAttributeName(getHost.getModel) match {
      case Some(name) ⇒ new RemoveAttributeCommand(node, name)
      case None       ⇒ null
    }
  }
}