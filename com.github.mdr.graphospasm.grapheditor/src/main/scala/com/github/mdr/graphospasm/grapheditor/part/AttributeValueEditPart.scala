package com.github.mdr.graphospasm.grapheditor.part

import org.eclipse.swt.accessibility.AccessibleEvent
import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.figure._
import org.eclipse.gef.editparts._
import org.eclipse.gef._
import org.eclipse.gef
import org.eclipse.draw2d._
import org.eclipse.draw2d.geometry._
import scala.collection.JavaConversions._
import java.util.{ List ⇒ JList }
import scala.collection.JavaConversions._
import org.eclipse.ui.views.properties.TextPropertyDescriptor
import org.eclipse.ui.views.properties.IPropertySource

class AttributeValueEditPart(val attributeValue: AttributeValue) extends NodeChildEditPart with Listener with SuspendableUpdates with IPropertySource {

  import AttributeValueEditPart._

  setModel(attributeValue)

  override def getFigure = super.getFigure.asInstanceOf[AttributeValueFigure]
  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
  override def getModel = super.getModel.asInstanceOf[AttributeValue]
  override def createFigure = new AttributeValueFigure

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new AttributeValueDirectEditPolicy)
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new AttributeValueComponentEditPolicy)
  }

  override def refreshVisuals() {
    getFigure.name = attributeValue.presentationString
    getParent.layoutChildren()
  }

  override def activate() {
    if (!isActive) attributeValue.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) attributeValue.removeListener(this)
    super.deactivate()
  }

  def changed(event: Event) {
    if (updatesSuspended)
      flagAsDirty()
    else
      refreshVisuals()
  }

  override def performRequest(request: Request) = request.getType match {
    case RequestConstants.REQ_OPEN | RequestConstants.REQ_DIRECT_EDIT ⇒
      Utils.withFont { font ⇒
        val nodeContentsLayoutInfo = NodeContentsLayouter.layout(getParent.getModel, font)
        val location = getFigure.getClientArea.getCopy
        location.width = nodeContentsLayoutInfo.attributeValueColumnWidth
        val cellEditorLocator = new FixedRegionCellEditorLocator(getFigure, location)
        new RenameEditManager(this, cellEditorLocator).show()
      }
    case _ ⇒
      super.performRequest(request)
  }

  override def getAccessibleEditPart(): AccessibleEditPart = new AccessibleGraphicalEditPart() {
    def getName(e: AccessibleEvent) { e.result = attributeValue.presentationString }
  }

  def getEditableValue = getModel

  def getPropertyDescriptors = Array(valueProperty, classProperty)

  def getPropertyValue(id: AnyRef) =
    if (id == valueProperty.getId)
      getModel.presentationString
    else if (id == classProperty.getId)
      getModel.value match {
        case o: AnyRef ⇒ o.getClass
        case _         ⇒ ""
      }
    else
      null

  def isPropertySet(id: AnyRef) = id == valueProperty.getId || id == classProperty.getId

  def resetPropertyValue(id: AnyRef) {}

  def setPropertyValue(id: AnyRef, value: AnyRef) = {
    if (id == valueProperty.getId)
      getModel.value = value
  }

}

object AttributeValueEditPart {

  val valueProperty = new TextPropertyDescriptor("com.github.mdr.graphospasm.grapheditor.property.attributeValue.value", "Value")
  val classProperty = new TextPropertyDescriptor("com.github.mdr.graphospasm.grapheditor.property.attributeValue.class", "Class")

}