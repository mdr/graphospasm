package com.github.mdr.graphospasm
package grapheditor.part

import grapheditor.model._
import grapheditor.model.commands._

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

  override def createFigure: IFigure = {
    val f = new FreeformLayer
    f.setLayoutManager(new FreeformLayout)
    f.setBorder(new MarginBorder(5))
    f
  }

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new GraphDiagramLayoutEditPolicy)
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

}

class GraphDiagramLayoutEditPolicy extends XYLayoutEditPolicy {

  override def getHost = super.getHost.asInstanceOf[GraphDiagramEditPart]

  protected def createChangeConstraintCommand(child: EditPart, constraint: Object): Command =
    new MoveNodeCommand(child.getModel.asInstanceOf[Node], constraint.asInstanceOf[Rectangle])

  protected def getCreateCommand(request: CreateRequest): Command = null

}
