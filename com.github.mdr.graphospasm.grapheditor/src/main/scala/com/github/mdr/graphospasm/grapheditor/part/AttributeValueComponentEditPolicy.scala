package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model.commands._
import org.eclipse.gef.commands.Command
import org.eclipse.gef.editpolicies.ComponentEditPolicy
import org.eclipse.gef.requests.GroupRequest
import org.eclipse.gef.Request

class AttributeValueComponentEditPolicy extends ComponentEditPolicy {

  override def getHost = super.getHost.asInstanceOf[AttributeValueEditPart]

  override protected def createDeleteCommand(deleteRequest: GroupRequest): RemoveAttributeCommand = {
    val viewer = getHost.getViewer
    val attributeValue = getHost.getModel
    val node = getHost.getParent.getModel

    for ((name, value) ← node.getAttributes if value == attributeValue)
      return new RemoveAttributeCommand(node, name)
    return null
  }

}