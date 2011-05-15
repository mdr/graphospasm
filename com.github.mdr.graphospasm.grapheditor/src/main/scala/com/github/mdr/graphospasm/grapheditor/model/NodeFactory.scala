package com.github.mdr.graphospasm.grapheditor.model

import com.github.mdr.graphospasm.core.graph.Name
import org.eclipse.draw2d.geometry._
import org.eclipse.gef.requests.CreationFactory

class NodeFactory extends CreationFactory {

  def getNewObject = {
    val node = new Node(Name("Vertex"))
    node.bounds = new Rectangle(new Point(0, 0), new Dimension(100, 100))
    node
  }

  def getObjectType = classOf[Node]

}
