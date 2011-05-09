package com.github.mdr.graphospasm
package core.graph

trait Graph {

  def vertices: List[Vertex]

  def edges: List[Edge]

  def incomingEdges(v: Vertex): List[Edge]

  def outgoingEdges(v: Vertex): List[Edge]

  def copy: Graph

}

trait Vertex {

  def name: Name

  def attributes: Map[Name, Any]

}

trait Edge {

  def nameOpt: Option[Name]

  def source: Vertex

  def target: Vertex

  def vertices = List(source, target)

  def isIncidentTo(v: Vertex): Boolean = vertices contains v

}
