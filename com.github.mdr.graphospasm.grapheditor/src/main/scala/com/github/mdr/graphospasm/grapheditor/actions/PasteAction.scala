package com.github.mdr.graphospasm.grapheditor.actions

import com.github.mdr.graphospasm.grapheditor.part._
import com.github.mdr.graphospasm.grapheditor.model.commands._
import PartialFunction._
import scala.collection.JavaConversions._
import org.eclipse.draw2d.geometry._
import org.eclipse.gef.ui.actions.Clipboard
import org.eclipse.gef.requests._
import org.eclipse.gef.{ NodeEditPart ⇒ _, _ }
import org.eclipse.gef.ui.actions.SelectionAction
import org.eclipse.ui.ISharedImages
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.actions.ActionFactory
import com.github.mdr.graphospasm.grapheditor.utils.Utils._
import com.github.mdr.graphospasm.grapheditor.model._

class PasteAction(part: IWorkbenchPart) extends SelectionAction(part) {

  setId(ActionFactory.PASTE.getId)
  setText("Paste")

  {
    val sharedImages = PlatformUI.getWorkbench.getSharedImages
    setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE))
    setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED))
  }

  def getSelectedDiagram: Option[GraphDiagram] = {
    for (selectedObject ← getSelectedObjects) selectedObject match {
      case p: GraphDiagramEditPart   ⇒ return Some(p.getModel)
      case p: NodeEditPart           ⇒ return Some(p.getParent.getModel)
      case p: AttributeNameEditPart  ⇒ return Some(p.getParent.getParent.getModel)
      case p: AttributeValueEditPart ⇒ return Some(p.getParent.getParent.getModel)
      case _                         ⇒
    }
    None
  }

  override def run() {
    for {
      graphDiagram ← getSelectedDiagram
      NodesAndConnections(nodes, connections) ← getClipboardNodesAndConnections
    } {

      var originalToClone: Map[Node, Node] = Map()
      val createNodeCommands = nodes.map { node ⇒
        val clonedNode = node.copy
        originalToClone += node -> clonedNode
        new CreateNodeCommand(clonedNode, node.bounds.getTopLeft.getTranslated(6, 6), node.size, graphDiagram)
      }
      val createConnectionCommands = connections.map { c ⇒
        val command = new CreateConnectionCommand(originalToClone(c.source), c.nameOpt)
        command.setTarget(originalToClone(c.target))
        command
      }
      execute(compoundCommand(createNodeCommands ++ createConnectionCommands))

      val viewer = getSelectedObjects.get(0).asInstanceOf[EditPart].getViewer
      viewer.deselectAll()
      viewer.flush()
      val editPartRegistry = viewer.getEditPartRegistry
      originalToClone.values.map(editPartRegistry.get(_).asInstanceOf[EditPart]).foreach(viewer.appendSelection)
    }
  }

  def getClipboardNodesAndConnections = condOpt(Clipboard.getDefault.getContents) { case nodesAndConnections: NodesAndConnections ⇒ nodesAndConnections }

  def calculateEnabled = getClipboardNodesAndConnections.isDefined

}

