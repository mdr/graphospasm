package com.github.mdr.graphospasm
package grapheditor.part

import grapheditor.model._
import grapheditor.figure._

import org.eclipse.gef.editparts._
import org.eclipse.gef._
import org.eclipse.gef
import org.eclipse.draw2d._
import org.eclipse.draw2d.geometry._
import scala.collection.JavaConversions._

class NodeEditPart(node: Node) extends AbstractGraphicalEditPart with Listener {

  setModel(node)

  override def getFigure = super.getFigure.asInstanceOf[NodeFigure]
  override def getParent = super.getParent.asInstanceOf[GraphicalEditPart]
  override def createFigure = new NodeFigure

  protected def createEditPolicies() {
    //    installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CreateConnectionsEditPolicy)
    //    installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeComponentEditPolicy)
    //    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new RenameDirectEditPolicy)
    //    installEditPolicy("Snap Feedback", new SnapFeedbackPolicy)
  }

  override def refreshVisuals() {
    println("refreshVisuals: " + this + ", parent = " + getParent)
    getFigure.name = node.name
    getFigure.attributes = node.attributes
    getParent.setLayoutConstraint(this, figure, node.bounds)
    // getParent.refresh()
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
    //    refreshChildren()
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
