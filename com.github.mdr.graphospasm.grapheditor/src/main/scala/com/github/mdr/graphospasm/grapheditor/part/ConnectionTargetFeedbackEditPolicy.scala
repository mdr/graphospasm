package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.figure.NodeFigure
import com.github.mdr.graphospasm.grapheditor.Plugin
import org.eclipse.swt.graphics.Color
import org.eclipse.draw2d.IFigure
import org.eclipse.gef.EditPart
import org.eclipse.gef.GraphicalEditPart
import org.eclipse.gef.Request
import org.eclipse.gef.RequestConstants._
import org.eclipse.gef.editpolicies.GraphicalEditPolicy
import PartialFunction._
import org.eclipse.gef.requests.CreateRequest
import com.github.mdr.graphospasm.grapheditor.ConnectionInProgress
import com.github.mdr.graphospasm.grapheditor.Attribute
import com.github.mdr.graphospasm.grapheditor.EdgeLabel

class ConnectionTargetFeedbackEditPolicy extends GraphicalEditPolicy {

  override def getHost = super.getHost.asInstanceOf[ConnectionEditPart]
  private final def getFigure = getHost.getFigure

  override def getTargetEditPart(request: Request): EditPart =
    if (request.getType == REQ_SELECTION_HOVER) getHost else null

  def showHighlight() {
    getFigure.targetFeedback = true
  }
  override def eraseTargetFeedback(request: Request) {
    getFigure.targetFeedback = false
  }

  override def showTargetFeedback(request: Request) {
    val highlight = cond(request.getType) {
      //      case REQ_MOVE | REQ_ADD | REQ_CLONE | REQ_CONNECTION_START | REQ_CONNECTION_END ⇒ true
      case REQ_CREATE ⇒ cond(request) {
        case createRequest: CreateRequest ⇒ cond(createRequest.getNewObject) {
          case _: EdgeLabel ⇒ getHost.getModel.nameOpt.isEmpty
        }
      }
    }
    if (highlight)
      showHighlight()
  }

}
