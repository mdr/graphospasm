package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.core.graph._
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

class NodeNameEditPart(name: NodeName) extends AbstractGraphicalEditPart with Listener {

  setModel(name)

  override def getFigure = super.getFigure.asInstanceOf[NodeNameFigure]
  override def getParent = super.getParent.asInstanceOf[GraphicalEditPart]
  override def createFigure = new NodeNameFigure

  protected def createEditPolicies() {
    //    installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CreateConnectionsEditPolicy)
    //    installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeComponentEditPolicy)
    //    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new RenameDirectEditPolicy)
    //    installEditPolicy("Snap Feedback", new SnapFeedbackPolicy)
  }

  override def refreshVisuals() {
    println("refreshVisuals: " + this + ", parent = " + getParent)
    getFigure.name = name
    // getParent.setLayoutConstraint(this, figure, node.bounds)
    // getParent.refresh()
  }

  override def activate() {
    if (!isActive) name.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) name.removeListener(this)
    super.deactivate()
  }

  def changed(event: Event) {
    refreshVisuals()
    //    refreshChildren()
    //    refreshSourceConnections()
    //    refreshTargetConnections()
  }

}
