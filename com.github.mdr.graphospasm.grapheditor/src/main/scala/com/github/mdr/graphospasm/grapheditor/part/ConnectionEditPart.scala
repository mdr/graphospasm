package com.github.mdr.graphospasm.grapheditor.part

import org.eclipse.draw2d.Label
import org.eclipse.draw2d.ConnectionLocator
import org.eclipse.draw2d.MidpointLocator
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d.IFigure
import org.eclipse.draw2d.PolylineConnection
import org.eclipse.gef.editparts.AbstractConnectionEditPart
import org.eclipse.draw2d.PolygonDecoration
import org.eclipse.gef.EditPolicy
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy
import com.github.mdr.graphospasm.grapheditor.figure.ConnectionFigure

class ConnectionEditPart(connection: Connection) extends AbstractConnectionEditPart {

  setModel(connection)

  override def getModel = super.getModel.asInstanceOf[Connection]

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy)
    installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy)
  }

  override protected def createFigure = new ConnectionFigure
}
