//package com.github.mdr.graphospasm.grapheditor.part
//
//import org.eclipse.gef.tools.SelectEditPartTracker
//import org.eclipse.swt.events.MouseEvent
//import org.eclipse.gef.tools.DragEditPartsTracker
//import com.github.mdr.graphospasm.core.graph._
//import com.github.mdr.graphospasm.grapheditor.model._
//import com.github.mdr.graphospasm.grapheditor.figure._
//
//import org.eclipse.gef.editparts._
//import org.eclipse.gef._
//import org.eclipse.gef
//import org.eclipse.draw2d.{ MouseEvent ⇒ _ }
//import org.eclipse.draw2d.geometry._
//import scala.collection.JavaConversions._
//import java.util.{ List ⇒ JList }
//import scala.collection.JavaConversions._
//import com.github.mdr.graphospasm.grapheditor.utils.Utils
//class NodeNameEditPart(nodeName: NodeName) extends AbstractNameEditPart(nodeName) {
//
//  override def getFigure = super.getFigure.asInstanceOf[NodeNameFigure]
//  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
//  override def getModel = super.getModel.asInstanceOf[NodeName]
//  override def createFigure = new NodeNameFigure
//
//  override def createEditPolicies() {
//    super.createEditPolicies()
//    installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeNameComponentEditPolicy)
//  }
//
//  override def performRequest(request: Request) = request.getType match {
//    case RequestConstants.REQ_OPEN | RequestConstants.REQ_DIRECT_EDIT ⇒
//      val editArea = getFigure.getClientArea.getCopy
//      //      if (editArea.width == 0)
//      //        editArea.width = 80
//      //      if (editArea.height == 0)
//      //        editArea.height = 80
//      val cellEditorLocator = new FixedRegionCellEditorLocator(getFigure, editArea)
//      new RenameEditManager(this, cellEditorLocator).show()
//    case _ ⇒
//      super.performRequest(request)
//  }
//
//}
