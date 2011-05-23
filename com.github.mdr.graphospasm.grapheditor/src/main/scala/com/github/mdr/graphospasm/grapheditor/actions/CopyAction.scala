package com.github.mdr.graphospasm.grapheditor.actions

import org.eclipse.gef.ui.actions.Clipboard
import org.eclipse.gef.ui.actions.SelectionAction
import org.eclipse.ui.ISharedImages
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.actions.ActionFactory
import scala.collection.JavaConversions._

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
    val clonedNodes = getSelectedObjects.toList.map(_.asInstanceOf[NodeEditPart].getModel.copy)
    Clipboard.getDefault.setContents(clonedNodes)
  }

  def calculateEnabled = getSelectedObjects.nonEmpty && getSelectedObjects.forall(_.isInstanceOf[NodeEditPart])

}