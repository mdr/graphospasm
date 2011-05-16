package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model.commands.AddAttributeCommand
import com.github.mdr.graphospasm.grapheditor.Attribute
import com.github.mdr.graphospasm.grapheditor.model.commands.DeleteNodeCommand
import org.eclipse.gef.requests.GroupRequest
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.graphics.GC
import org.eclipse.gef.editpolicies.NonResizableEditPolicy
import org.eclipse.gef.commands.Command
import org.eclipse.gef.requests.CreateRequest
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy
import com.github.mdr.graphospasm.grapheditor.model.NodeName
import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.figure._

import org.eclipse.gef.editparts._
import org.eclipse.gef._
import org.eclipse.gef
import org.eclipse.draw2d._
import org.eclipse.draw2d.geometry._
import scala.collection.JavaConversions._
import java.util.{ List ⇒ JList }
import scala.collection.JavaConversions._
import scala.math.{ min, max }

class NodeEditPart(node: Node) extends AbstractGraphicalEditPart with Listener {

  setModel(node)

  private def toList[T](p: (T, T)): List[T] = List(p._1, p._2)

  override protected def getModelChildren: JList[AnyRef] = List(node.name) ++ node.getAttributes.flatMap(toList)

  override def getFigure = super.getFigure.asInstanceOf[NodeFigure]
  override def getParent = super.getParent.asInstanceOf[GraphicalEditPart]
  override def getModel = super.getModel.asInstanceOf[Node]
  override def createFigure = new NodeFigure

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new NodeLayoutEditPolicy)
    installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CreateConnectionsEditPolicy)
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeComponentEditPolicy)
  }

  override def refreshVisuals() {
    getFigure.name = node.name.name.simpleName
    getFigure.attributes = node.attributes
    getParent.setLayoutConstraint(this, getFigure, node.bounds)
    // getParent.refresh()
  }

  private var nodeNameEditPart: NodeNameEditPart = _
  private var attributeNameEditParts: Map[AttributeName, AttributeNameEditPart] = Map()
  private var attributeValueEditParts: Map[AttributeValue, AttributeValueEditPart] = Map()

  private def findCurrentChildEditParts() {
    attributeNameEditParts = Map()
    attributeValueEditParts = Map()
    for (child ← getChildren)
      child match {
        case editPart: NodeNameEditPart ⇒
          this.nodeNameEditPart = editPart
        case editPart: AttributeNameEditPart ⇒
          attributeNameEditParts = attributeNameEditParts + (editPart.attributeName -> editPart)
        case editPart: AttributeValueEditPart ⇒
          attributeValueEditParts = attributeValueEditParts + (editPart.attributeValue -> editPart)
      }

  }

  private def layoutChildren() {
    findCurrentChildEditParts()
    val contentArea = getFigure.getContentArea(node.bounds)
    val font = nodeNameEditPart.getFigure.getFont

    val nameHeight = {
      val text = nodeNameEditPart.getModel.name.simpleName
      val textDimension = FigureUtilities.getTextExtents(text, font)
      val textX = max((contentArea.width - textDimension.width) / 2, 0)
      val nameHeight = textDimension.height + 2
      val textWidth = min(textDimension.width, contentArea.width)
      val bounds = new Rectangle(textX, 2, textWidth, textDimension.height + 2)
      getFigure.setConstraint(nodeNameEditPart.getFigure, bounds)
      nameHeight
    }

    val attributeMap = node.getAttributes.toMap
    if (attributeMap.nonEmpty && attributeNameEditParts.size == attributeMap.size && attributeValueEditParts.size == attributeMap.size) {
      val sortedAttributeNames = attributeMap.toList.map(_._1).sortBy(p ⇒ p.name.simpleName)
      val widestAttributeName = sortedAttributeNames.map { attributeName ⇒
        val nameText = attributeName.name.simpleName
        FigureUtilities.getTextExtents(nameText, font).width
      }.max
      val widestAttributeValue = sortedAttributeNames.map { attributeName ⇒
        val valueText = attributeMap(attributeName).value.toString
        FigureUtilities.getTextExtents(valueText, font).width
      }.max

      var currentY = 2 + nameHeight + 4

      for ((attributeName, attributeValue) ← attributeMap.toList.sortBy(_._1.name.simpleName)) {
        val attributeNameDimension = {
          val text = attributeName.name.simpleName
          val dimension = FigureUtilities.getTextExtents(text, font)
          val bounds = new Rectangle(4, currentY, dimension.width, dimension.height)
          getFigure.setConstraint(attributeNameEditParts(attributeName).getFigure, bounds)
          dimension
        }
        {
          val text = attributeValue.value.toString
          val dimension = FigureUtilities.getTextExtents(text, font)
          val startX = widestAttributeName + 17
          val width = min(contentArea.width - startX, dimension.width)
          val bounds = new Rectangle(startX, currentY, width, dimension.height)
          getFigure.setConstraint(attributeValueEditParts(attributeValue).getFigure, bounds)
        }
        currentY += attributeNameDimension.height
      }
    }

  }

  def relayout() {
    layoutChildren()
  }

  override def activate() {
    if (!isActive) node.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) node.removeListener(this)
    super.deactivate()
  }

  def changed(event: Event) {
    refreshVisuals()
    for (child ← getChildren) {
      child.asInstanceOf[NodeChildEditPart].refreshVisuals()
    }
    refreshChildren()
    refreshSourceConnections()
    refreshTargetConnections()
  }

  override protected def getModelSourceConnections = node.sourceConnections
  override protected def getModelTargetConnections = node.targetConnections

  def getSourceConnectionAnchor(request: Request) = getFigure.connectionAnchor
  def getTargetConnectionAnchor(request: Request) = getFigure.connectionAnchor
  def getTargetConnectionAnchor(connection: gef.ConnectionEditPart) = getFigure.connectionAnchor
  def getSourceConnectionAnchor(connection: gef.ConnectionEditPart) = getFigure.connectionAnchor

}

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
