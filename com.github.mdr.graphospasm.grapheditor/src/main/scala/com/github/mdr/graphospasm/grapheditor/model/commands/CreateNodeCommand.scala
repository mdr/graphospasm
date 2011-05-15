package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.model.Node
import com.github.mdr.graphospasm.grapheditor.model.GraphDiagram
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

object CreateNodeCommand {

  val INITIAL_NODE_DIMENSION = new Dimension(200, 64)

}

class CreateNodeCommand(node: Node, location: Point, size: Dimension, graphDiagram: GraphDiagram) extends Command {

  import CreateNodeCommand._

  private val baseName = node.name

  override def execute() {
    val initialLocation = new Rectangle(location, Option(size) getOrElse INITIAL_NODE_DIMENSION)
    node.bounds = initialLocation
    graphDiagram.add(node)
  }

  override def undo() {
    graphDiagram.remove(node)
  }

}
