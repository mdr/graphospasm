package com.github.mdr.graphospasm.grapheditor.part

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

  override protected def getModelChildren: JList[AnyRef] = List(node.name)

  override def getFigure = super.getFigure.asInstanceOf[NodeFigure]
  override def getParent = super.getParent.asInstanceOf[GraphicalEditPart]
  override def createFigure = new NodeFigure

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new NodeLayoutEditPolicy)
    //    installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CreateConnectionsEditPolicy)
    //    installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeComponentEditPolicy)
    //    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new RenameDirectEditPolicy)
    //    installEditPolicy("Snap Feedback", new SnapFeedbackPolicy)
  }

  override def refreshVisuals() {
    getFigure.name = node.name.name.simpleName
    getFigure.attributes = node.attributes
    getParent.setLayoutConstraint(this, getFigure, node.bounds)
    // getParent.refresh()
  }

  def setLayoutConstraint(attributeNameEditPart: AttributeNameEditPart) {

  }
  def setLayoutConstraint(attributeValueEditPart: AttributeValueEditPart) {

  }

  def setLayoutConstraint(nodeNameEditPart: NodeNameEditPart) {
    val nodeNameFigure = nodeNameEditPart.getFigure
    val text = nodeNameEditPart.getModel.name.simpleName

    val textDimension = FigureUtilities.getTextExtents(text, nodeNameFigure.getFont)
    val contentArea = getFigure.getContentArea(node.bounds)
    val textX = max((contentArea.width - textDimension.width) / 2, 0)
    val textWidth = min(textDimension.width, contentArea.width)
    getFigure.setConstraint(nodeNameFigure, new Rectangle(textX, 2, textWidth, textDimension.height + 2))
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
    refreshChildren()
    //    refreshSourceConnections()
    //    refreshTargetConnections()
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

  protected def getCreateCommand(request: CreateRequest): Command = null

  override def createChildEditPolicy(child: EditPart) = {
    val policy = new NonResizableEditPolicy
    policy.setDragAllowed(false)
    policy
  }

}
