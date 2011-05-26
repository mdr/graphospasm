package com.github.mdr.graphospasm.grapheditor.actions

import java.util.Iterator
import java.util.List
import scala.collection.JavaConversions._
import com.github.mdr.graphospasm.grapheditor.part._
import com.github.mdr.graphospasm.grapheditor.utils.Utils._
import org.eclipse.gef.ui.actions.Clipboard
import org.eclipse.gef.requests._
import org.eclipse.gef.{ NodeEditPart ⇒ _, _ }
import org.eclipse.gef.ui.actions.SelectionAction
import org.eclipse.ui.ISharedImages
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.actions.ActionFactory
import com.github.mdr.graphospasm.grapheditor.model._

class CutAction(part: IWorkbenchPart) extends SelectionAction(part) {

  setId(ActionFactory.CUT.getId)
  setText("Cut")

  {
    val sharedImages = PlatformUI.getWorkbench.getSharedImages
    setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT))
    setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED))
  }

  override def run() {

    var originalToClone: Map[Node, Node] = Map()
    val originalNodes = getSelectedObjects.toList.map(_.asInstanceOf[NodeEditPart].getModel)
    val clonedNodes = originalNodes.map { node ⇒
      val clonedNode = node.copy
      originalToClone += node -> clonedNode
      clonedNode
    }

    // TODO: duplication with clone nodes command & copy action
    val clonedConnections = for {
      originalNode ← originalNodes
      connection ← originalNode.sourceConnections // Just source connections to avoid double counting
      clonedSource ← originalToClone.get(connection.source)
      clonedTarget ← originalToClone.get(connection.target)
    } yield Connection.create(clonedSource, clonedTarget, connection.nameOpt)
    Clipboard.getDefault.setContents(NodesAndConnections(clonedNodes, clonedConnections))

    val selectedEditParts = getSelectedObjects.toList.map(_.asInstanceOf[NodeEditPart])
    val request = new GroupRequest(RequestConstants.REQ_DELETE)
    request.setEditParts(selectedEditParts)
    val commands = selectedEditParts.flatMap { part ⇒ Option(part.getCommand(request)) }
    execute(compoundCommand(commands))
  }

  def calculateEnabled = getSelectedObjects.nonEmpty && getSelectedObjects.forall(_.isInstanceOf[NodeEditPart])

}
