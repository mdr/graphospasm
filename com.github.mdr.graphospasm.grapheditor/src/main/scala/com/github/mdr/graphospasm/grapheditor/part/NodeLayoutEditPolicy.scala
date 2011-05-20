package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.Attribute
import org.eclipse.gef.editpolicies.NonResizableEditPolicy
import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.model.commands._
import org.eclipse.gef.requests.CreateRequest
import org.eclipse.gef.commands.Command
import org.eclipse.gef.EditPart
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy

class NodeLayoutEditPolicy extends XYLayoutEditPolicy {

  override def getHost = super.getHost.asInstanceOf[NodeEditPart]

  protected def createChangeConstraintCommand(child: EditPart, constraint: Object): Command = null

  protected def getCreateCommand(request: CreateRequest): Command = {
    val node = getHost.getModel
    val newObjectClass = request.getNewObjectType.asInstanceOf[Class[_]]
    if (newObjectClass == classOf[Attribute]) {
      new AddAttributeCommand(node)
    } else
      null
  }

  override def createChildEditPolicy(child: EditPart) = {
    val policy = new NonResizableEditPolicy
    policy.setDragAllowed(false)
    policy
  }

}
