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
import com.github.mdr.graphospasm.grapheditor.part.ConnectionEditPart
import com.github.mdr.graphospasm.grapheditor.model.commands.SetEdgeLabelCommand
import com.github.mdr.graphospasm.grapheditor.utils.Utils._
import com.github.mdr.graphospasm.grapheditor.part.NodeEditPart
import com.github.mdr.graphospasm.grapheditor.part.GraphDiagramEditPart
import com.github.mdr.graphospasm.grapheditor.model.commands.RelayoutCommand
import com.github.mdr.graphospasm.grapheditor.Plugin

object RemoveEdgeLabelAction {

  val actionId = "removeEdgeLabelAction"

}

class RemoveEdgeLabelAction(part: IWorkbenchPart) extends SelectionAction(part) {

  import RemoveEdgeLabelAction._
  setText("Remove label from edge")
  setId(actionId)
  setToolTipText("Remove label from edge")
  // setImageDescriptor(Plugin.addEdgeLabel16)
  // setHoverImageDescriptor(getImageDescriptor)

  def calculateEnabled = getSelectedObjects.forall(cond(_) {
    case part: ConnectionEditPart ⇒ part.getModel.nameOpt.isDefined
  })

  private final def getCommand = compoundCommand(getSelectedObjects.toList collect {
    case part: ConnectionEditPart ⇒ new SetEdgeLabelCommand(part.getModel, None)
  })

  override def run() = execute(getCommand)

}
