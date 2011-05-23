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
    for (graphDiagram ← getSelectedDiagram)
      Clipboard.getDefault.getContents match {
        case ListOfNodes(nodes) ⇒
          val commands = nodes.map { node ⇒ new CreateNodeCommand(node.copy, node.bounds.getTopLeft.getTranslated(6, 6), node.size, graphDiagram) }
          execute(compoundCommand(commands))
      }
  }

  object ListOfNodes {

    def unapply(obj: AnyRef): Option[List[Node]] = obj match {
      case Nil | _ :: _ ⇒
        val xs = obj.asInstanceOf[List[AnyRef]]
        if (xs forall { _.isInstanceOf[Node] })
          Some(xs map { _.asInstanceOf[Node] })
        else
          None
      case _ ⇒ None
    }

  }

  def calculateEnabled = cond(Clipboard.getDefault.getContents) { case ListOfNodes(nodes) ⇒ true }

}

