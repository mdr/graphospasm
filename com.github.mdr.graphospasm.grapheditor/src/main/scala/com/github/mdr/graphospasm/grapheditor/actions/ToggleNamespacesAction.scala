package com.github.mdr.graphospasm.grapheditor.actions

import org.eclipse.jface.util.IPropertyChangeListener
import org.eclipse.jface.action.Action
import org.eclipse.jface.action.IAction
import org.eclipse.gef.GraphicalViewer
import PartialFunction._
import org.eclipse.gef.ui.parts.GraphicalEditor
import com.github.mdr.graphospasm.grapheditor.GraphDiagramEditor

object ToggleNamespacesAction {

  val actionId = "toggleNamespacesAction"

  val property = "showNamespaces"

}

class ToggleNamespacesAction(editor: GraphDiagramEditor) extends Action("Toggle namespaces", IAction.AS_CHECK_BOX) {
  import ToggleNamespacesAction._

  setId(actionId)

  override def isChecked = cond(editor.getGraphicalViewer.getProperty(property)) {
    case b: java.lang.Boolean ⇒ b
  }

  override def run() {
    editor.getGraphicalViewer.setProperty(property, !isChecked)
  }

}