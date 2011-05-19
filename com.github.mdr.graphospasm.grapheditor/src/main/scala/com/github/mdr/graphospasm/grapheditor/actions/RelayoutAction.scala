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

object RelayoutAction {

  val requestId = "relayoutRequest"

  val actionId = "relayoutAction"

}

class RelayoutAction(part: IWorkbenchPart) extends SelectionAction(part) {
  import RelayoutAction._
  setText("Relayout")
  setId(actionId)
  setToolTipText("Relayout the contents of the diagram")
  setImageDescriptor(Plugin.relayout16)
  setHoverImageDescriptor(getImageDescriptor)

  def calculateEnabled = true //getSelectedObjects.nonEmpty && getSelectedObjects.forall(_.isInstanceOf[NodeContainerEditPart])

  private def getCommand: RelayoutCommand = {
    for (selectedObject ← getSelectedObjects)
      selectedObject match {
        case part: GraphDiagramEditPart ⇒ return new RelayoutCommand(part.getModel)
        case nodeEditPart: NodeEditPart ⇒ return new RelayoutCommand(nodeEditPart.getModel.diagram)
        case _                          ⇒
      }
    null
  }

  override def run() = execute(getCommand)

}
