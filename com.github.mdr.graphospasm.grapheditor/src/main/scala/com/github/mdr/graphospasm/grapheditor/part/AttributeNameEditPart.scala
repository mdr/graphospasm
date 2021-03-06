package com.github.mdr.graphospasm.grapheditor.part

import org.eclipse.swt.accessibility.AccessibleEvent
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
import com.github.mdr.graphospasm.grapheditor.utils.Utils
import org.eclipse.ui.views.properties.TextPropertyDescriptor
import org.eclipse.ui.views.properties.IPropertySource
import PartialFunction._
import org.eclipse.jface.viewers.ICellEditorValidator

class AttributeNameEditPart(val attributeName: AttributeName) extends NodeChildEditPart with Listener with SuspendableUpdates with IPropertySource {
  import AttributeNameEditPart._
  setModel(attributeName)

  override def refreshVisuals() {
    val name = attributeName.name
    getFigure.name = getParent.getParent.namespacePrefixManager.getDisplayName(name)
    val namespaceSuffix = if (name.namespace == "") "" else " (" + name.namespace + ")"
    getFigure.toolTipText = name.simpleName + namespaceSuffix

    //    getFigure.name = attributeName.name.simpleName
    getParent.layoutChildren()
  }

  override def activate() {
    if (!isActive) attributeName.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) attributeName.removeListener(this)
    super.deactivate()
  }

  def changed(event: Event) {
    if (updatesSuspended)
      flagAsDirty()
    else
      refreshVisuals()
  }

  override def getFigure = super.getFigure.asInstanceOf[AttributeNameFigure]
  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
  override def getModel = super.getModel.asInstanceOf[AttributeName]
  override def createFigure = new AttributeNameFigure

  override def createEditPolicies() {
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new AttributeNameComponentEditPolicy)
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new AttributeNameDirectEditPolicy)
  }

  override def performRequest(request: Request) = request.getType match {
    case RequestConstants.REQ_OPEN | RequestConstants.REQ_DIRECT_EDIT ⇒
      Utils.withFont { font ⇒
        val nodeContentsLayoutInfo = NodeContentsLayouter.layout(getParent.getModel, font)
        val location = getFigure.getClientArea.getCopy
        location.width = nodeContentsLayoutInfo.attributeNameColumnWidth
        val cellEditorLocator = new FixedRegionCellEditorLocator(getFigure, location)
        new RenameAttributeNameEditManager(this, cellEditorLocator, Some(simpleNameValidator)).show()
      }
    case _ ⇒
      super.performRequest(request)
  }

  override def getAccessibleEditPart(): AccessibleEditPart = new AccessibleGraphicalEditPart() {
    def getName(e: AccessibleEvent) { e.result = attributeName.name.simpleName }
  }

  def getEditableValue = getModel

  val simpleNameProperty = new TextPropertyDescriptor(SIMPLE_NAME_PROPERTY_ID, "Simple name")

  val namespaceProperty = new TextPropertyDescriptor(NAMESPACE_PROPERTY_ID, "Namespace")

  val simpleNameValidator = new ICellEditorValidator {
    def isValid(value: AnyRef) = {
      val candidateName = Name(value.toString, getModel.name.namespace)
      val attributes = getParent.getModel.getAttributes
      val colliding = attributes.exists {
        case (attributeName, _) ⇒ attributeName != getModel && attributeName.name == candidateName
      }
      if (colliding)
        "Name would collide with an existing attribute name"
      else
        null
    }
  }

  simpleNameProperty.setValidator(simpleNameValidator)

  namespaceProperty.setValidator(new ICellEditorValidator {
    def isValid(value: AnyRef) = {
      val candidateName = Name(getModel.name.simpleName, value.toString)
      val attributes = getParent.getModel.getAttributes
      val colliding = attributes.exists {
        case (attributeName, _) ⇒ attributeName != getModel && attributeName.name == candidateName
      }
      if (colliding)
        "Name would collide with an existing attribute name"
      else
        null
    }
  })
  def getPropertyDescriptors = Array(simpleNameProperty, namespaceProperty)

  def getPropertyValue(id: AnyRef) = id match {
    case SIMPLE_NAME_PROPERTY_ID ⇒ getModel.name.simpleName
    case NAMESPACE_PROPERTY_ID   ⇒ getModel.name.namespace
  }

  def isPropertySet(id: AnyRef) = cond(id) { case SIMPLE_NAME_PROPERTY_ID | NAMESPACE_PROPERTY_ID ⇒ true }

  def resetPropertyValue(id: AnyRef) {}

  def setPropertyValue(id: AnyRef, value: AnyRef) = id match {
    case SIMPLE_NAME_PROPERTY_ID ⇒
      getModel.name = getModel.name.copy(simpleName = value.toString)
    case NAMESPACE_PROPERTY_ID ⇒
      getModel.name = getModel.name.copy(namespace = value.toString)
  }

}

object AttributeNameEditPart {

  val NAMESPACE_PROPERTY_ID = "com.github.mdr.graphospasm.grapheditor.property.attributeName.namespace"

  val SIMPLE_NAME_PROPERTY_ID = "com.github.mdr.graphospasm.grapheditor.property.attributeName.simpleName"

}