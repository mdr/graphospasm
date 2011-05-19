package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.figure.NodeFigure
import com.github.mdr.graphospasm.grapheditor.Plugin
import org.eclipse.swt.graphics.Color
import org.eclipse.draw2d.IFigure
import org.eclipse.gef.EditPart
import org.eclipse.gef.GraphicalEditPart
import org.eclipse.gef.Request
import org.eclipse.gef.RequestConstants
import org.eclipse.gef.editpolicies.GraphicalEditPolicy

class TargetFeedbackEditPolicy extends GraphicalEditPolicy {

  override def eraseTargetFeedback(request: Request) = {
    getContainerFigure.targetFeedback = false
  }

  private final def getContainerBackground = getContainerFigure.getBackgroundColor

  private final def getContainerFigure = getHost.asInstanceOf[GraphicalEditPart].getFigure.asInstanceOf[NodeFigure]

  override def getTargetEditPart(request: Request): EditPart =
    if (request.getType == RequestConstants.REQ_SELECTION_HOVER) getHost else null

  def showHighlight() { getContainerFigure.targetFeedback = true }

  override def showTargetFeedback(request: Request) {
    import RequestConstants._
    request.getType match {
      case REQ_MOVE | REQ_ADD | REQ_CLONE | /* REQ_CONNECTION_START | REQ_CONNECTION_END | */ REQ_CREATE ⇒
        showHighlight()
      case _ ⇒
    }
  }

}
