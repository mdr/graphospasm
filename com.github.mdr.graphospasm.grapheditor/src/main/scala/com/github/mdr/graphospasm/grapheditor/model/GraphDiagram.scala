package com.github.mdr.graphospasm.grapheditor.model

class GraphDiagram extends Observable {

  private var nodes_ : List[Node] = Nil

  def nodes = nodes_

  def add(node: Node) {
    insert(node, nodes_.length)
  }

  def insert(node: Node, index: Int) {
    nodes_ = nodes_.patch(index, List(node), 0)
    node.setDiagram(this)
    fireEvent(NodeInserted(node, index))
  }

  def remove(node: Node) {
    val index = indexOf(node)
    nodes_ = nodes_ filterNot { _ == node }
    node.unsetDiagram()
    fireEvent(NodeRemoved(node, index))
  }

  def indexOf(thing: Node) = nodes_ indexOf thing

}

