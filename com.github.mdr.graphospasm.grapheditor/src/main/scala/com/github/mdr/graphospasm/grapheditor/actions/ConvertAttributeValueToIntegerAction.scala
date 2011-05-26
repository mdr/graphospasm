package com.github.mdr.graphospasm.grapheditor.actions

import scala.collection.JavaConversions._
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.gef.EditPart
import org.eclipse.gef.Request
import org.eclipse.gef.commands._
import org.eclipse.gef.ui.actions.SelectionAction
import scala.collection.JavaConversions._
import PartialFunction._
import com.github.mdr.graphospasm.grapheditor.part._
import com.github.mdr.graphospasm.grapheditor.model.commands.SetEdgeLabelCommand
import com.github.mdr.graphospasm.grapheditor.utils.Utils._
import com.github.mdr.graphospasm.grapheditor.part.NodeEditPart
import com.github.mdr.graphospasm.grapheditor.part.GraphDiagramEditPart
import com.github.mdr.graphospasm.grapheditor.model.commands.RelayoutCommand
import com.github.mdr.graphospasm.grapheditor.Plugin
import com.github.mdr.graphospasm.grapheditor.model.commands.SetAttributeValueCommand

object ConvertAttributeValueToIntegerAction {

  val actionId = "convertAttributeValueToIntegerAction"

}

class ConvertAttributeValueToIntegerAction(part: IWorkbenchPart) extends SelectionAction(part) {

  import ConvertAttributeValueToIntegerAction._
  setText("Convert attribute value to integer")
  setId(actionId)
  setToolTipText("Convert attribute value to integer")
  // setImageDescriptor(Plugin.addEdgeLabel16)
  // setHoverImageDescriptor(getImageDescriptor)

  def calculateEnabled = getSelectedObjects.forall(cond(_) {
    case part: AttributeValueEditPart ⇒ !part.getModel.value.isInstanceOf[java.lang.Integer]
  })

  private final def getCommand = compoundCommand(getSelectedObjects.toList collect {
    case part: AttributeValueEditPart ⇒
      val attributeValue = part.getModel
      val currentText = ("" + attributeValue.value)
      val newValue =
        try Integer.parseInt(currentText)
        catch { case e: NumberFormatException ⇒ 0 }
      new SetAttributeValueCommand(attributeValue, newValue: java.lang.Integer)
  })

  override def run() = execute(getCommand)

}
