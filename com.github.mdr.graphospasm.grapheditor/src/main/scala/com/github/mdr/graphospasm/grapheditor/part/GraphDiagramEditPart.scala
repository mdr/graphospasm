package com.github.mdr.graphospasm.grapheditor.part

import org.eclipse.gef.AccessibleEditPart
import org.eclipse.gef.commands.CompoundCommand
import org.eclipse.gef.Request
import org.eclipse.gef.requests.ChangeBoundsRequest
import org.eclipse.gef.CompoundSnapToHelper
import org.eclipse.gef.SnapToGrid
import org.eclipse.gef.SnapToGuides
import org.eclipse.gef.SnapToGeometry
import org.eclipse.gef.rulers.RulerProvider
import PartialFunction.cond
import org.eclipse.gef.SnapToHelper
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy
import com.github.mdr.graphospasm.grapheditor.figure.GraphDiagramFigure
import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.model.commands._

import org.eclipse.swt.SWT
import org.eclipse.gef.LayerConstants
import org.eclipse.gef.requests.CreateRequest
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command
import org.eclipse.gef.EditPart
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy
import org.eclipse.gef.EditPolicy

import org.eclipse.swt.accessibility._
import org.eclipse.gef.editparts._
import org.eclipse.draw2d._

import java.util.{ List ⇒ JList }
import scala.collection.JavaConversions._

class GraphDiagramEditPart(diagram: GraphDiagram) extends AbstractGraphicalEditPart with Listener {

  setModel(diagram)

  override protected def getModelChildren: JList[Node] = diagram.nodes
  override def getModel: GraphDiagram = diagram

  override def createFigure: IFigure = new GraphDiagramFigure

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new GraphDiagramLayoutEditPolicy)
    installEditPolicy("Snap Feedback", new SnapFeedbackPolicy)
  }

  override def activate() {
    if (!isActive) diagram.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) diagram.removeListener(this)
    super.deactivate()
  }

  def changed(event: Event) {
    refreshVisuals()
    refreshChildren()
  }

  override def getAccessibleEditPart(): AccessibleEditPart = new AccessibleGraphicalEditPart() {
    def getName(e: AccessibleEvent) { e.result = "diagram" }
  }

  override def getAdapter(type_ : Class[_]) =
    if (type_ == classOf[SnapToHelper])
      makeSnapToStrategies
    else
      super.getAdapter(type_)

  protected def makeSnapToStrategies() = {
    def checkProperty(p: String) = cond(getViewer.getProperty(p)) { case x if x == true ⇒ true }
    var strategies: List[SnapToHelper] = Nil
    if (checkProperty(RulerProvider.PROPERTY_RULER_VISIBILITY))
      strategies ::= new SnapToGuides(this)
    if (checkProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED))
      strategies ::= new SnapToGeometry(this)
    if (checkProperty(SnapToGrid.PROPERTY_GRID_ENABLED))
      strategies ::= new SnapToGrid(this)
    strategies match {
      case Nil     ⇒ null
      case List(s) ⇒ s
      case xs      ⇒ new CompoundSnapToHelper(xs.reverse.toArray[SnapToHelper])
    }
  }

}
