package com.github.mdr.graphospasm.grapheditor.part
import com.github.mdr.graphospasm.grapheditor.utils.Utils
import org.eclipse.swt.accessibility.AccessibleEvent
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

class NodeEditPart(node: Node) extends AbstractGraphicalEditPart with Listener with org.eclipse.gef.NodeEditPart {

  setModel(node)

  private def toList[T](p: (T, T)): List[T] = List(p._1, p._2)

  override protected def getModelChildren: JList[AnyRef] = node.getAttributes.flatMap(toList)

  override def getFigure = super.getFigure.asInstanceOf[NodeFigure]
  override def getParent = super.getParent.asInstanceOf[GraphicalEditPart]
  override def getModel = super.getModel.asInstanceOf[Node]
  override def createFigure = new NodeFigure

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new NodeLayoutEditPolicy)
    installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CreateConnectionsEditPolicy)
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeComponentEditPolicy)
    installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new TargetFeedbackEditPolicy)
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new NodeDirectEditPolicy)
  }

  override def refreshVisuals() {
    for (nameBounds ← layoutChildren())
      getFigure.nameBounds = nameBounds
    getFigure.name = node.name.simpleName
    getFigure.hasAttributes = node.getAttributes.nonEmpty
    getParent.setLayoutConstraint(this, getFigure, node.bounds)
    // getParent.refresh()
  }

  private var attributeNameEditParts: Map[AttributeName, AttributeNameEditPart] = Map()
  private var attributeValueEditParts: Map[AttributeValue, AttributeValueEditPart] = Map()

  private def findCurrentChildEditParts() {
    attributeNameEditParts = Map()
    attributeValueEditParts = Map()
    for (child ← getChildren)
      child match {
        case editPart: AttributeNameEditPart ⇒
          attributeNameEditParts = attributeNameEditParts + (editPart.attributeName -> editPart)
        case editPart: AttributeValueEditPart ⇒
          attributeValueEditParts = attributeValueEditParts + (editPart.attributeValue -> editPart)
      }

  }

  /**
   * @return bounds for name
   */
  def layoutChildren(): Option[Rectangle] = {
    findCurrentChildEditParts()
    if (attributeNameEditParts.size == node.getAttributes.size && attributeValueEditParts.size == node.getAttributes.size)
      Utils.withFont { font ⇒
        val layoutInfo = NodeContentsLayouter.layout(node, font)
        for ((attributeName, bounds) ← layoutInfo.attributeNameBounds)
          getFigure.setConstraint(attributeNameEditParts(attributeName).getFigure, bounds)
        for ((attributeValue, bounds) ← layoutInfo.attributeValueBounds)
          getFigure.setConstraint(attributeValueEditParts(attributeValue).getFigure, bounds)
        getFigure.nameBounds = layoutInfo.nameBounds
        Some(layoutInfo.nameBounds)
      }
    else
      None
  }

  override def activate() {
    if (!isActive) node.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) node.removeListener(this)
    super.deactivate()
  }

  override def refreshChildren() = super.refreshChildren()

  def changed(event: Event) {
    refreshChildren()
    refreshVisuals()
    for (child ← getChildren) {
      child.asInstanceOf[NodeChildEditPart].refreshVisuals()
    }
    refreshSourceConnections()
    refreshTargetConnections()
  }

  override protected def getModelSourceConnections = node.sourceConnections
  override protected def getModelTargetConnections = node.targetConnections

  def getSourceConnectionAnchor(request: Request) = getFigure.connectionAnchor
  def getTargetConnectionAnchor(request: Request) = getFigure.connectionAnchor
  def getTargetConnectionAnchor(connection: gef.ConnectionEditPart) = getFigure.connectionAnchor
  def getSourceConnectionAnchor(connection: gef.ConnectionEditPart) = getFigure.connectionAnchor

  override def getAccessibleEditPart(): AccessibleEditPart = new AccessibleGraphicalEditPart() {
    def getName(e: AccessibleEvent) { e.result = node.name.simpleName }
  }

  override def performRequest(request: Request) = request.getType match {
    case RequestConstants.REQ_OPEN | RequestConstants.REQ_DIRECT_EDIT ⇒
      val editArea = getFigure.nameLabel.getClientArea.getCopy
      val cellEditorLocator = new FixedRegionCellEditorLocator(getFigure.nameLabel, editArea)
      new RenameNodeNameEditManager(this, cellEditorLocator).show()
    case _ ⇒
      super.performRequest(request)
  }

}
