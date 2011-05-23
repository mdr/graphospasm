package com.github.mdr.graphospasm.grapheditor.actions

import com.github.mdr.graphospasm.grapheditor.part.NodeEditPart
import com.github.mdr.graphospasm.grapheditor.part.GraphDiagramEditPart
import com.github.mdr.graphospasm.grapheditor.model.commands.RelayoutCommand
import com.github.mdr.graphospasm.grapheditor.Plugin
import java.util.List
import scala.collection.JavaConversions._
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.gef.EditPart
import org.eclipse.gef.Request
import org.eclipse.gef.commands._
import org.eclipse.gef.ui.actions.SelectionAction
import scala.collection.JavaConversions._
import com.github.mdr.graphospasm.grapheditor.part.ConnectionEditPart
import PartialFunction._
import com.github.mdr.graphospasm.grapheditor.model.commands.SetEdgeLabelCommand

object AddEdgeLabelAction {

  //  val requestId = "relayoutRequest"

  val actionId = "addEdgeLabelAction"

}

class AddEdgeLabelAction(part: IWorkbenchPart) extends SelectionAction(part) {
  import AddEdgeLabelAction._
  setText("Add label to edge")
  setId(actionId)
  setToolTipText("Add a label to an edge")
  setImageDescriptor(Plugin.addEdgeLabel16)
  setHoverImageDescriptor(getImageDescriptor)

  def calculateEnabled =
    getSelectedObjects.size == 1 && cond(getSelectedObjects.get(0)) {
      case part: ConnectionEditPart ⇒ part.getModel.nameOpt.isEmpty
    }

  private def getCommand = {
    val connection = getSelectedObjects.get(0).asInstanceOf[ConnectionEditPart].getModel
    new SetEdgeLabelCommand(connection, Some("label"))
  }

  override def run() = execute(getCommand)

}
