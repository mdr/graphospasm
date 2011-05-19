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

class ConnectionEditPart(connection: Connection) extends AbstractConnectionEditPart {

  setModel(connection)

  protected def createEditPolicies() {
    installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy)
    installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy)
  }

  override protected def createFigure: IFigure = {
    val polylineConnection = new PolylineConnection
    polylineConnection.setLineWidth(2)
    val decoration = new PolygonDecoration
    decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP)
    polylineConnection.setTargetDecoration(decoration)

    val label = new Label("middle")
    //    polylineConnection.add(label, new ConnectionLocator(polylineConnection, ConnectionLocator.MIDDLE))

    polylineConnection
  }

}
