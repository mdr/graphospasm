package com.github.mdr.graphospasm.grapheditor.actions

import com.github.mdr.graphospasm.grapheditor.part.NodeEditPart
import com.github.mdr.graphospasm.grapheditor.part.GraphDiagramEditPart
import com.github.mdr.graphospasm.grapheditor.model.commands.RelayoutCommand
import com.github.mdr.graphospasm.grapheditor.Plugin
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
import org.eclipse.gef.RequestConstants

object AddEdgeLabelAction {

  val actionId = "addEdgeLabelAction"

}

class AddEdgeLabelAction(part: IWorkbenchPart) extends SelectionAction(part) {
  import AddEdgeLabelAction._
  setText("Add label to edge")
  setId(actionId)
  setToolTipText("Add a label to an edge")
  setImageDescriptor(Plugin.addEdgeLabel16)
  setHoverImageDescriptor(getImageDescriptor)

  def calculateEnabled = getConnectionEditPartOpt.isDefined

  private def getConnectionEditPartOpt: Option[ConnectionEditPart] = condOpt(getSelectedObjects.toList) {
    case List(part: ConnectionEditPart) ⇒ part
  }

  override def run() = {
    val connectionEditPart = getConnectionEditPartOpt.get
    val connection = connectionEditPart.getModel
    val command = new SetEdgeLabelCommand(connection, Some("label"))

    execute(command)

    connectionEditPart.getFigure.validate()
    connectionEditPart.performRequest(new Request(RequestConstants.REQ_DIRECT_EDIT))
  }

}
