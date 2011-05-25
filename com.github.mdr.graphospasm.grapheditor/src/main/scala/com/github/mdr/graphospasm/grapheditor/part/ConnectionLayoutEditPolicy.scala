package com.github.mdr.graphospasm.grapheditor.part
import org.eclipse.gef.editpolicies.LayoutEditPolicy
import org.eclipse.gef.editpolicies.NonResizableEditPolicy
import org.eclipse.gef.EditPart
import org.eclipse.gef.requests.CreateRequest
import org.eclipse.gef.commands.Command
import com.github.mdr.graphospasm.grapheditor.model.commands.SetEdgeLabelCommand
import org.eclipse.gef.Request
import com.github.mdr.graphospasm.grapheditor.EdgeLabel
import com.github.mdr.graphospasm.grapheditor.model.Connection

class ConnectionLayoutEditPolicy extends LayoutEditPolicy {

  override def createChildEditPolicy(child: EditPart) = new NonResizableEditPolicy

  def getCreateCommand(request: CreateRequest): Command = {
    val connection = getHost.getModel.asInstanceOf[Connection]
    val newObjectClass = request.getNewObjectType.asInstanceOf[Class[_]]
    if (newObjectClass == classOf[EdgeLabel] && connection.nameOpt.isEmpty)
      new SetEdgeLabelCommand(connection, Some("label"))
    else
      null
  }

  def getMoveChildrenCommand(request: Request) = null

}