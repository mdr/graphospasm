package com.github.mdr.graphospasm.grapheditor.part

import org.eclipse.swt.accessibility.AccessibleEvent
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
import com.github.mdr.graphospasm.grapheditor.utils.Utils

class AttributeNameEditPart(val attributeName: AttributeName) extends NodeChildEditPart with Listener {

  setModel(attributeName)

  override def refreshVisuals() {
    getFigure.name = attributeName.name.simpleName
    getParent.layoutChildren()
  }

  override def activate() {
    if (!isActive) attributeName.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) attributeName.removeListener(this)
    super.deactivate()
  }

  def changed(event: Event) {
    refreshVisuals()
  }

  override def getFigure = super.getFigure.asInstanceOf[AttributeNameFigure]
  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
  override def getModel = super.getModel.asInstanceOf[AttributeName]
  override def createFigure = new AttributeNameFigure

  override def createEditPolicies() {
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new AttributeNameComponentEditPolicy)
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new AttributeNameDirectEditPolicy)
  }

  override def performRequest(request: Request) = request.getType match {
    case RequestConstants.REQ_OPEN | RequestConstants.REQ_DIRECT_EDIT ⇒
      Utils.withFont { font ⇒
        val nodeContentsLayoutInfo = NodeContentsLayouter.layout(getParent.getModel, font)
        val location = getFigure.getClientArea.getCopy
        location.width = nodeContentsLayoutInfo.attributeNameColumnWidth
        val cellEditorLocator = new FixedRegionCellEditorLocator(getFigure, location)
        new RenameEditManager(this, cellEditorLocator).show()
      }
    case _ ⇒
      super.performRequest(request)
  }

  override def getAccessibleEditPart(): AccessibleEditPart = new AccessibleGraphicalEditPart() {
    def getName(e: AccessibleEvent) { e.result = attributeName.name.simpleName }
  }

}
