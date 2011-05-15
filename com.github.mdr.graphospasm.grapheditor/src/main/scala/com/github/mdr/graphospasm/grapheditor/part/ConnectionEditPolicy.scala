package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.model.commands.DeleteConnectionCommand
import org.eclipse.gef.commands.Command
import org.eclipse.gef.requests.GroupRequest

class ConnectionEditPolicy extends org.eclipse.gef.editpolicies.ConnectionEditPolicy {

  def getDeleteCommand(request: GroupRequest): Command =
    new DeleteConnectionCommand(getHost.getModel.asInstanceOf[Connection])

}

