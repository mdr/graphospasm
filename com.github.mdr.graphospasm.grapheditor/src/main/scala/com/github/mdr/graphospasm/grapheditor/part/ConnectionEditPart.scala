package com.github.mdr.graphospasm.grapheditor.part

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
    // installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy())
    installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy())
  }

  override protected def createFigure: IFigure = {
    val connection = new PolylineConnection
    connection.setLineWidth(1)
    val decoration = new PolygonDecoration
    decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP)
    connection.setTargetDecoration(decoration)
    connection
  }

}
