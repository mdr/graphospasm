package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.model.commands._
import org.eclipse.gef.commands.Command
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy
import org.eclipse.gef.requests.CreateConnectionRequest
import org.eclipse.gef.requests.ReconnectRequest

class CreateConnectionsEditPolicy extends GraphicalNodeEditPolicy {

  protected def getConnectionCompleteCommand(request: CreateConnectionRequest) = {
    val result = request.getStartCommand.asInstanceOf[CreateConnectionCommand]
    result.setTarget(getHost.getModel)
    result
  }

  override def getHost = super.getHost.asInstanceOf[NodeEditPart]

  protected def getConnectionCreateCommand(request: CreateConnectionRequest) = {
    val result = new CreateConnectionCommand(getHost.getModel)
    request.setStartCommand(result)
    result
  }

  protected def getReconnectTargetCommand(request: ReconnectRequest) = null

  protected def getReconnectSourceCommand(request: ReconnectRequest) = null

}
