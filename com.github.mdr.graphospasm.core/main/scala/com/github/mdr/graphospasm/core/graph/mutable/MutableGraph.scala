package com.github.mdr.graphospasm
package core.graph.mutable

import core.graph._

trait MutableGraph extends Graph {

  def vertices: List[MutableVertex]

  def edges: List[MutableEdge]

  def incomingEdges(v: Vertex): List[MutableEdge]

  def outgoingEdges(v: Vertex): List[MutableEdge]

  def copy: MutableGraph

  def addVertex(name: Name): MutableVertex

  def removeVertex(v: MutableVertex)

  def addEdge(source: MutableVertex, target: MutableVertex, nameOpt: Option[Name] = None): MutableEdge

  def removeEdge(edge: MutableEdge)

}

trait MutableVertex extends Vertex {

  def setName(name: Name)

  def setAttribute(name: Name, value: Any)

  def removeAttribute(name: Name)

}

trait MutableEdge extends Edge {

  def source: MutableVertex

  def target: MutableVertex

  def setName(name: Name)

}
