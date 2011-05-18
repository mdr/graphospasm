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

abstract class AbstractNameEditPart(nodeName: AbstractName) extends NodeChildEditPart with Listener {

  setModel(nodeName)

  override def getFigure = super.getFigure.asInstanceOf[AbstractNameFigure]
  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
  override def getModel = super.getModel.asInstanceOf[AbstractName]

  override def refreshVisuals() {
    getFigure.name = nodeName.name.simpleName
    getParent.relayout()
  }

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new EditNameDirectEditPolicy)
  }

  //  override def getTargetEditPart(request: Request) = request.getType match {
  //    case RequestConstants.REQ_SELECTION ⇒ null
  //    case _ ⇒ super.getTargetEditPart(request)
  //  }

  override def activate() {
    if (!isActive) nodeName.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) nodeName.removeListener(this)
    super.deactivate()
  }

  def changed(event: Event) {
    refreshVisuals()
  }

  // TODO: Delete, override in subclasses
  override def performRequest(request: Request) = request.getType match {
    case RequestConstants.REQ_OPEN | RequestConstants.REQ_DIRECT_EDIT ⇒
      val cellEditorLocator = new FixedRegionCellEditorLocator(getFigure, getFigure.getClientArea)
      new RenameEditManager(this, cellEditorLocator).show()
    case _ ⇒
      super.performRequest(request)
  }

}
