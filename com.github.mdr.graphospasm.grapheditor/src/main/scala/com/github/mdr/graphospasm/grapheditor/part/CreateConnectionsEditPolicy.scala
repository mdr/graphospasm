package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.model.commands._
import org.eclipse.gef.commands.Command
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy
import org.eclipse.gef.requests.CreateConnectionRequest
import org.eclipse.gef.requests.ReconnectRequest
import org.eclipse.gef.LayerConstants
import org.eclipse.gef.Request
import com.github.mdr.graphospasm.grapheditor.figure.ConnectionFigure

class CreateConnectionsEditPolicy extends GraphicalNodeEditPolicy {

  protected def getConnectionCreateCommand(request: CreateConnectionRequest) = {
    val result = new CreateConnectionCommand(getHost.getModel)
    request.setStartCommand(result)
    result
  }
  protected def getConnectionCompleteCommand(request: CreateConnectionRequest) = {
    val result = request.getStartCommand.asInstanceOf[CreateConnectionCommand]
    result.setTarget(getHost.getModel)
    result
  }

  override def getHost = super.getHost.asInstanceOf[NodeEditPart]

  protected def getReconnectTargetCommand(request: ReconnectRequest) = request.getTarget match {
    case nodeEditPart: NodeEditPart ⇒
      new ReconnectTargetCommand(request.getConnectionEditPart.asInstanceOf[ConnectionEditPart].getModel, nodeEditPart.getModel)
    case _ ⇒ null
  }

  protected def getReconnectSourceCommand(request: ReconnectRequest) = request.getTarget match {
    case nodeEditPart: NodeEditPart ⇒
      new ReconnectSourceCommand(request.getConnectionEditPart.asInstanceOf[ConnectionEditPart].getModel, nodeEditPart.getModel)
    case _ ⇒ null
  }

  override def getFeedbackLayer = getLayer(LayerConstants.SCALED_FEEDBACK_LAYER)

  override def createDummyConnection(req: Request) = new ConnectionFigure

}
