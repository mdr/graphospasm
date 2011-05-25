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

class NodeLayoutEditPolicy extends XYLayoutEditPolicy {

  override def getHost = super.getHost.asInstanceOf[NodeEditPart]

  protected def createChangeConstraintCommand(child: EditPart, constraint: Object): Command = null

  protected def getCreateCommand(request: CreateRequest): Command = {
    val node = getHost.getModel
    val newObjectClass = request.getNewObjectType.asInstanceOf[Class[_]]
    if (newObjectClass == classOf[Attribute]) {

      val existingAttributes = node.getAttributes.map(_._1.name.simpleName).toSet
      var candidateName = "name"
      var i = 0
      while (existingAttributes contains candidateName) {
        i += 1
        candidateName = "name" + i
      }
      new AddAttributeCommand(node, new AttributeName(Name(candidateName)), new AttributeValue("value"))
    } else
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
    } yield new OrphanAttributeCommand(node, attributeName)
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

  override def getAddCommand(req: Request) = {
    val commands = for {
      editPart ← getEditParts(req)
      (attributeName, attributeValue) ← getAttributeNameValue(editPart)
    } yield new AddAttributeCommand(getHost.getModel, attributeName, attributeValue)
    compoundCommand(commands)
  }

  def getEditParts(groupRequest: Request): List[EditPart] =
    groupRequest.asInstanceOf[GroupRequest].getEditParts.collect { case part: EditPart ⇒ part }.toList

  override def getCloneCommand(request: ChangeBoundsRequest): Command = {
    val attributeNameAndValues = getEditParts(request).flatMap(getAttributeNameValue).distinct
    new CloneAttributesCommand(getHost.getModel, attributeNameAndValues)
  }

}
