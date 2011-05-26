package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.Attribute
import com.github.mdr.graphospasm.grapheditor.utils.Utils._
import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.model.commands._
import com.github.mdr.graphospasm.core.graph.Name
import org.eclipse.gef.editpolicies.NonResizableEditPolicy
import org.eclipse.gef.commands.Command
import org.eclipse.gef.EditPart
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy
import org.eclipse.gef.requests._
import org.eclipse.gef.Request
import org.eclipse.draw2d.geometry.Rectangle
import scala.collection.JavaConversions._
import PartialFunction._
import org.eclipse.gef.commands.UnexecutableCommand

class NodeLayoutEditPolicy extends XYLayoutEditPolicy {

  override def getHost = super.getHost.asInstanceOf[NodeEditPart]

  protected def createChangeConstraintCommand(child: EditPart, constraint: Object): Command = null

  protected def getCreateCommand(request: CreateRequest): Command = {
    val node = getHost.getModel
    val newObjectClass = request.getNewObjectType.asInstanceOf[Class[_]]
    if (newObjectClass == classOf[Attribute])
      AddAttributeCommand.create(node)
    else
      null
  }

  override def createChildEditPolicy(child: EditPart) = {
    val policy = new NonResizableEditPolicy
    policy.setDragAllowed(true)
    policy
  }

  override def getOrphanChildrenCommand(request: Request) = {
    val node = getHost.getModel
    val commands = for {
      editPart ← getEditParts(request)
      (attributeName, attributeValue) ← getAttributeNameValue(editPart)
    } yield new DeleteAttributeCommand(node, attributeName)
    compoundCommand(commands)
  }

  def getAttributeNameValue(editPart: EditPart): Option[(AttributeName, AttributeValue)] = condOpt(editPart) {
    case part: AttributeNameEditPart ⇒
      val node = part.getParent.getModel
      val attributeName = part.getModel
      (attributeName, node.getAttributeValue(part.getModel))
    case part: AttributeValueEditPart ⇒
      val node = part.getParent.getModel
      val attributeValue = part.getModel
      (node.getAttributeName(attributeValue).get, part.getModel)
  }

  override def getAddCommand(request: Request) = {
    val attributeNameAndValues = getEditParts(request).flatMap(getAttributeNameValue).distinct
    val targetNode = getHost.getModel
    val existingNames = targetNode.getAttributes.toMap.keySet.map(_.name).toSet
    val newNames = attributeNameAndValues.map(_._1.name)
    if (newNames.exists(existingNames))
      UnexecutableCommand.INSTANCE
    else {
      val commands = for ((attributeName, attributeValue) ← attributeNameAndValues)
        yield new AddAttributeCommand(targetNode, attributeName, attributeValue)
      compoundCommand(commands)
    }
  }

  def getEditParts(groupRequest: Request): List[EditPart] =
    groupRequest.asInstanceOf[GroupRequest].getEditParts.toList.collect { case part: EditPart ⇒ part }

  override def getCloneCommand(request: ChangeBoundsRequest): Command = {
    val attributeNameAndValues = getEditParts(request).flatMap(getAttributeNameValue).distinct
    val targetNode = getHost.getModel
    val existingNames = targetNode.getAttributes.toMap.keySet.map(_.name).toSet
    val newNames = attributeNameAndValues.map(_._1.name)
    if (newNames.exists(existingNames))
      null
    else
      new CloneAttributesCommand(targetNode, attributeNameAndValues)
  }

}
