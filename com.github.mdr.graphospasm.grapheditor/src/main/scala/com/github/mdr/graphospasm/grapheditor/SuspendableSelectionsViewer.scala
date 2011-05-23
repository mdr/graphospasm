package com.github.mdr.graphospasm.grapheditor
import com.github.mdr.graphospasm.grapheditor.part._
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.StructuredSelection
import scala.collection.JavaConversions._

class SuspendableSelectionsViewer extends ScrollingGraphicalViewer {

  var suspendSelectionEvents = false

  override def fireSelectionChanged() {
    if (!suspendSelectionEvents)
      super.fireSelectionChanged()
  }

  private object StructuredSelectionDestructor {
    def unapply(selection: ISelection) = selection match {
      case structuredSelection: StructuredSelection ⇒
        Some(structuredSelection.iterator.toList.asInstanceOf[List[AnyRef]])
      case _ ⇒
        None
    }
  }

  // We want to avoid selections that grab both node and node children edit parts:
  override def setSelection(selection: ISelection) {
    super.setSelection(
      selection match {
        case StructuredSelectionDestructor(selectionContents) if (selectionContents.exists(_.isInstanceOf[NodeEditPart])) ⇒
          new StructuredSelection(selectionContents.filter(_.isInstanceOf[NodeEditPart]).toArray)
        case _ ⇒
          selection
      })
  }

}