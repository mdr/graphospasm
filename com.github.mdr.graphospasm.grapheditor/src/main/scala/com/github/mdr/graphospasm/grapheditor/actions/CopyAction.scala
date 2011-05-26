package com.github.mdr.graphospasm.grapheditor.actions

import org.eclipse.gef.ui.actions.Clipboard
import org.eclipse.gef.ui.actions.SelectionAction
import org.eclipse.ui.ISharedImages
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.actions.ActionFactory
import scala.collection.JavaConversions._
import com.github.mdr.graphospasm.grapheditor.model._

import com.github.mdr.graphospasm.grapheditor.part.NodeEditPart

class CopyAction(part: IWorkbenchPart) extends SelectionAction(part) {

  setId(ActionFactory.COPY.getId)
  setText("Copy")

  {
    val sharedImages = PlatformUI.getWorkbench.getSharedImages
    setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY))
    setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED))
  }

  override def run() {

    var originalToClone: Map[Node, Node] = Map()
    val originalNodes = getSelectedObjects.toList.map(_.asInstanceOf[NodeEditPart].getModel)
    val clonedNodes = originalNodes.map { node ⇒
      val clonedNode = node.copy
      originalToClone += node -> clonedNode
      clonedNode
    }

    // TODO: duplication with clone nodes command
    val clonedConnections = for {
      originalNode ← originalNodes
      connection ← originalNode.sourceConnections // Just source connections to avoid double counting
      clonedSource ← originalToClone.get(connection.source)
      clonedTarget ← originalToClone.get(connection.target)
    } yield Connection.create(clonedSource, clonedTarget, connection.nameOpt)
    Clipboard.getDefault.setContents(NodesAndConnections(clonedNodes, clonedConnections))
  }

  def calculateEnabled = getSelectedObjects.nonEmpty && getSelectedObjects.forall(_.isInstanceOf[NodeEditPart])

}