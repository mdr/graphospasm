package com.github.mdr.graphospasm.grapheditor.actions

import scala.collection.JavaConversions._
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.gef.EditPart
import org.eclipse.gef.Request
import org.eclipse.gef.commands._
import org.eclipse.gef.ui.actions.SelectionAction
import scala.collection.JavaConversions._
import PartialFunction._
import com.github.mdr.graphospasm.grapheditor.part._
import com.github.mdr.graphospasm.grapheditor.model.commands.SetEdgeLabelCommand
import com.github.mdr.graphospasm.grapheditor.utils.Utils._
import com.github.mdr.graphospasm.grapheditor.part.NodeEditPart
import com.github.mdr.graphospasm.grapheditor.part.GraphDiagramEditPart
import com.github.mdr.graphospasm.grapheditor.model.commands.RelayoutCommand
import com.github.mdr.graphospasm.grapheditor.Plugin
import com.github.mdr.graphospasm.grapheditor.model.commands.AddAttributeCommand
import org.eclipse.gef.RequestConstants

object AddAttributeAction {

  val actionId = "addAttributeAction"

}

class AddAttributeAction(part: IWorkbenchPart) extends SelectionAction(part) {

  import AddAttributeAction._
  setText("Add attribute")
  setId(actionId)
  setToolTipText("Add attribute to a vertex")
  setImageDescriptor(Plugin.addAttribute16Descriptor)
  setHoverImageDescriptor(getImageDescriptor)

  def calculateEnabled = cond(getSelectedObjects.toList) {
    case List(part: NodeEditPart) ⇒ true
  }

  // assumes calculateEnabled == true 
  private final def getSelectedNodeEditPart = {
    val List(part: NodeEditPart) = getSelectedObjects.toList
    part
  }

  override def run() = {
    val nodeEditPart = getSelectedNodeEditPart
    val node = nodeEditPart.getModel
    val addAttributeCommand = AddAttributeCommand.create(node)
    execute(addAttributeCommand)

    nodeEditPart.refreshVisuals()
    nodeEditPart.refreshChildren()
    nodeEditPart.getParent.getFigure.validate()
    nodeEditPart.getFigure.validate()

    val editPartRegistry = nodeEditPart.getViewer.getEditPartRegistry
    val attributeNameEditPart = editPartRegistry.get(addAttributeCommand.attributeName).asInstanceOf[AttributeNameEditPart]
    attributeNameEditPart.performRequest(new Request(RequestConstants.REQ_DIRECT_EDIT))
  }

}
