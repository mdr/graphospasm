package com.github.mdr.graphospasm.grapheditor.part

import org.eclipse.draw2d.Label
import org.eclipse.draw2d.ConnectionLocator
import org.eclipse.draw2d.MidpointLocator
import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.core._
import org.eclipse.draw2d.IFigure
import org.eclipse.draw2d.PolylineConnection
import org.eclipse.gef.editparts.AbstractConnectionEditPart
import org.eclipse.draw2d.PolygonDecoration
import org.eclipse.gef.EditPolicy
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy
import com.github.mdr.graphospasm.grapheditor.figure.ConnectionFigure
import org.eclipse.ui.views.properties.IPropertySource
import org.eclipse.ui.views.properties.TextPropertyDescriptor
import org.eclipse.gef.Request
import org.eclipse.gef.RequestConstants
import com.github.mdr.graphospasm.grapheditor.utils.Utils
import org.eclipse.gef.GraphicalEditPart
import org.eclipse.gef.tools.CellEditorLocator
import org.eclipse.gef.tools.DirectEditManager
import org.eclipse.swt.widgets.Composite
import org.eclipse.jface.viewers.TextCellEditor
import org.eclipse.gef.editpolicies.LayoutEditPolicy
import org.eclipse.gef.EditPart
import org.eclipse.gef.editpolicies.NonResizableEditPolicy
import org.eclipse.gef.requests.CreateRequest
import org.eclipse.gef.commands.Command
import com.github.mdr.graphospasm.grapheditor.EdgeLabel
import com.github.mdr.graphospasm.grapheditor.model.commands.SetEdgeLabelCommand

class ConnectionEditPart(connection: Connection) extends AbstractConnectionEditPart with Listener with SuspendableUpdates with IPropertySource {

  import ConnectionEditPart._

  setModel(connection)

  override def getModel = super.getModel.asInstanceOf[Connection]
  override def getFigure = super.getFigure.asInstanceOf[ConnectionFigure]
  override protected def createFigure = new ConnectionFigure

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy)
    installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy)
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ConnectionDirectEditPolicy)
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new ConnectionLayoutEditPolicy)
    installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ConnectionTargetFeedbackEditPolicy)
  }

  override def refreshVisuals() {
    super.refreshVisuals()
    connection.nameOpt match {
      case Some(name) ⇒ getFigure.setLabel(name.simpleName)
      case None       ⇒ getFigure.noLabel()
    }
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
        val cellEditorLocator = new FixedRegionCellEditorLocator(getFigure, getFigure.label.getClientArea)
        new ConnectionDirectEditManager(this, cellEditorLocator).show()
      }
    case _ ⇒
      super.performRequest(request)
  }

  override def activate() {
    if (!isActive) connection.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) connection.removeListener(this)
    super.deactivate()
  }

  def getEditableValue = getModel

  def getPropertyDescriptors = if (connection.nameOpt.isDefined) Array(simpleNameProperty, namespaceProperty) else Array()

  def getPropertyValue(id: AnyRef) =
    if (id == simpleNameProperty.getId)
      getModel.nameOpt.get.simpleName
    else if (id == namespaceProperty.getId)
      getModel.nameOpt.get.namespace
    else
      null

  def isPropertySet(id: AnyRef) =
    id == simpleNameProperty.getId || id == namespaceProperty.getId

  def resetPropertyValue(id: AnyRef) {}

  def setPropertyValue(id: AnyRef, value: AnyRef) = {
    if (id == simpleNameProperty.getId)
      getModel.nameOpt = Some(getModel.nameOpt.get.copy(simpleName = value.toString))
    else if (id == namespaceProperty.getId)
      getModel.nameOpt = Some(getModel.nameOpt.get.copy(namespace = value.toString))
  }

}

object ConnectionEditPart {

  val simpleNameProperty = new TextPropertyDescriptor("com.github.mdr.graphospasm.grapheditor.property.node.simpleName", "Simple name")
  val namespaceProperty = new TextPropertyDescriptor("com.github.mdr.graphospasm.grapheditor.property.node.namespace", "Namespace")

}

class ConnectionDirectEditManager(source: GraphicalEditPart, locator: CellEditorLocator) extends DirectEditManager(source, null, locator) {

  override def createCellEditorOn(composite: Composite) = getEditPart.getModel.nameOpt match {
    case Some(_) ⇒ new TextCellEditor(composite /*, SWT.MULTI | SWT.WRAP */ )
    case None    ⇒ null
  }

  override def getEditPart = super.getEditPart.asInstanceOf[ConnectionEditPart]

  override def initCellEditor() {
    val connection = getEditPart.getModel
    getCellEditor.setValue(connection.nameOpt.get.simpleName)
  }

}
