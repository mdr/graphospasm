package com.github.mdr.graphospasm.grapheditor.part

import org.eclipse.swt.events.MouseEvent
import org.eclipse.gef.tools.DragEditPartsTracker
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.figure._

import org.eclipse.gef.editparts._
import org.eclipse.gef._
import org.eclipse.gef
import org.eclipse.draw2d.{ MouseEvent ⇒ _ }
import org.eclipse.draw2d.geometry._
import scala.collection.JavaConversions._
import java.util.{ List ⇒ JList }
import scala.collection.JavaConversions._
import com.github.mdr.graphospasm.grapheditor.utils.Utils
class NodeNameEditPart(nodeName: NodeName) extends AbstractNameEditPart(nodeName) {

  override def getFigure = super.getFigure.asInstanceOf[NodeNameFigure]
  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
  override def getModel = super.getModel.asInstanceOf[NodeName]
  override def createFigure = new NodeNameFigure

  override def createEditPolicies() {
    super.createEditPolicies()
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeNameComponentEditPolicy)
  }

  override def performRequest(request: Request) = request.getType match {
    case RequestConstants.REQ_OPEN | RequestConstants.REQ_DIRECT_EDIT ⇒
      val editArea = getFigure.getClientArea.getCopy
      //      if (editArea.width == 0)
      //        editArea.width = 80
      //      if (editArea.height == 0)
      //        editArea.height = 80
      val cellEditorLocator = new FixedRegionCellEditorLocator(getFigure, editArea)
      new RenameEditManager(this, cellEditorLocator).show()
    case _ ⇒
      super.performRequest(request)
  }

  override def getDragTracker(request: Request): DragTracker = new DragEditPartsTracker2

  class DragEditPartsTracker2 extends DragEditPartsTracker(getParent) {

    val thisDragTracker = new DragEditPartsTracker(NodeNameEditPart.this) {

      override def createOperationSet() = super.createOperationSet() // List(getParent)

    }

    override def isMove() = true

    override def createOperationSet() = List(getParent)

    override def mouseDrag(mouseEvent: MouseEvent, viewer: EditPartViewer) = super.mouseDrag(mouseEvent, viewer)

    override def mouseDoubleClick(mouseEvent: MouseEvent, viewer: EditPartViewer) = thisDragTracker.mouseDoubleClick(mouseEvent, viewer)

    override def mouseDown(mouseEvent: MouseEvent, viewer: EditPartViewer) = {
      super.mouseDown(mouseEvent, viewer)
      thisDragTracker.mouseDown(mouseEvent, viewer)
    }

    override def mouseUp(mouseEvent: MouseEvent, viewer: EditPartViewer) = {
      thisDragTracker.mouseUp(mouseEvent, viewer)
      super.mouseUp(mouseEvent, viewer)
    }

    override def activate() {
      thisDragTracker.activate()
      super.activate()
    }

    override def setEditDomain(domain: EditDomain) {
      thisDragTracker.setEditDomain(domain)
      super.setEditDomain(domain)
    }

    override def setViewer(viewer: EditPartViewer) {
      thisDragTracker.setViewer(viewer)
      super.setViewer(viewer)
    }

  }

  class DragEditPartsTracker1 extends DragEditPartsTracker(this) {

    val parentDelegateDragTracker = new DragEditPartsTracker(getParent) {
      override def createOperationSet() = List(getParent)

      override def isMove() = true

    }

    override def mouseDrag(mouseEvent: MouseEvent, viewer: EditPartViewer) = parentDelegateDragTracker.mouseDrag(mouseEvent, viewer)

    override def mouseDown(mouseEvent: MouseEvent, viewer: EditPartViewer) = {
      parentDelegateDragTracker.mouseDown(mouseEvent, viewer)
      super.mouseDown(mouseEvent, viewer)
    }

    override def mouseUp(mouseEvent: MouseEvent, viewer: EditPartViewer) = {
      parentDelegateDragTracker.mouseUp(mouseEvent, viewer)
      super.mouseUp(mouseEvent, viewer)
    }

    override def activate() {
      parentDelegateDragTracker.activate()
      super.activate()
    }

    override def setEditDomain(domain: EditDomain) {
      parentDelegateDragTracker.setEditDomain(domain)
      super.setEditDomain(domain)
    }

    override def setViewer(viewer: EditPartViewer) {
      parentDelegateDragTracker.setViewer(viewer)
      super.setViewer(viewer)
    }

  }
}
