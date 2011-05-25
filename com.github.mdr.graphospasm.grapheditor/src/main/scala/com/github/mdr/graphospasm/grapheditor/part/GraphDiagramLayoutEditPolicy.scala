package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.model.NodeContentsLayouter
import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.grapheditor.model.commands.MoveNodeCommand
import com.github.mdr.graphospasm.grapheditor.model.commands.CreateNodeCommand
import com.github.mdr.graphospasm.grapheditor.model.Node
import scala.collection.JavaConversions._
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.gef.Request
import org.eclipse.gef.requests.ChangeBoundsRequest
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.CompoundCommand
import org.eclipse.gef.requests.CreateRequest
import org.eclipse.gef.commands.Command
import org.eclipse.gef.EditPart
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy
import com.github.mdr.graphospasm.grapheditor.model.commands.CloneNodesCommand
import scala.collection.JavaConversions._

object GraphDiagramLayoutEditPolicy {

  val defaultNodeSize = new Dimension(120, 36)
  val minimumNodeSize = new Dimension(120, 36)

}

class GraphDiagramLayoutEditPolicy extends XYLayoutEditPolicy {

  import GraphDiagramLayoutEditPolicy._

  override def getHost = super.getHost.asInstanceOf[GraphDiagramEditPart]

  protected def createChangeConstraintCommand(child: EditPart, constraint: Object): Command = {
    val node = child.getModel.asInstanceOf[Node]
    val newBounds = constraint.asInstanceOf[Rectangle]
    new MoveNodeCommand(node, newBounds)
  }

  protected def getCreateCommand(request: CreateRequest): Command = {
    val diagram = getHost.getModel
    val newObjectClass = request.getNewObjectType.asInstanceOf[Class[_]]
    if (newObjectClass == classOf[Node]) {
      val adjustedLocation = getConstraintFor(request).asInstanceOf[Rectangle]
      val size = if (request.getSize == null) defaultNodeSize else minimumNodeSize getUnioned adjustedLocation.getSize
      new CreateNodeCommand(request.getNewObject.asInstanceOf[Node], adjustedLocation.getTopLeft,
        size.getCopy, diagram)
    } else
      null
  }

  override def getCloneCommand(request: ChangeBoundsRequest): Command = {
    val nodes: Map[Node, Rectangle] = request.getEditParts.collect {
      case part: NodeEditPart ⇒ (part.getModel, getConstraintForClone(part, request).asInstanceOf[Rectangle])
    }.toMap
    new CloneNodesCommand(getHost.getModel, nodes)
  }

}
