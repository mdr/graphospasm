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

class NodeNameEditPart(nodeName: NodeName) extends NodeChildEditPart with Listener {

  setModel(nodeName)

  override def getFigure = super.getFigure.asInstanceOf[NodeNameFigure]
  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
  override def getModel = super.getModel.asInstanceOf[NodeName]
  override def createFigure = new NodeNameFigure

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new RenameDirectEditPolicy)
  }

  override def refreshVisuals() {
    getFigure.name = nodeName.name.simpleName
    getParent.setLayoutConstraint(this)
  }

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

  override def performRequest(request: Request) = {
    // println("KnowledgeNodeEditPart: " + request)
    if (request.getType == RequestConstants.REQ_OPEN || request.getType == RequestConstants.REQ_DIRECT_EDIT)
      new RenameEditManager(this, new RenameCellEditorLocator(getFigure.asInstanceOf[NodeNameFigure])).show()
    else
      super.performRequest(request)
  }

}
