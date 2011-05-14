package com.github.mdr.graphospasm.core.graph.mutable

import com.github.mdr.graphospasm.core.graph._
import com.google.common.collect.{ ListMultimap, ArrayListMultimap }

import scala.collection.JavaConversions._

object MutableGraphImpl {

  def copy(graph: Graph): MutableGraph = {
    val clone = new MutableGraphImpl
    var oldToNewVertexMap: Map[Vertex, MutableVertex] = Map()
    for (oldVertex ← graph.vertices) {
      val newVertex = clone.addVertex(oldVertex.name)
      for ((key, value) ← oldVertex.attributes)
        newVertex.setAttribute(key, value)
      oldToNewVertexMap = oldToNewVertexMap + (oldVertex -> newVertex)
    }
    for (oldEdge ← graph.edges)
      clone.addEdge(oldToNewVertexMap(oldEdge.source), oldToNewVertexMap(oldEdge.target), oldEdge.nameOpt)
    clone
  }

  def emptyGraph: MutableGraph = new MutableGraphImpl

}

class MutableGraphImpl extends MutableGraph {

  private var vertices_ : List[MutableVertexImpl] = Nil

  private var edges_ : List[MutableEdge] = Nil

  private val incomingEdgeMap: ListMultimap[MutableVertex, MutableEdgeImpl] = ArrayListMultimap.create()

  private val outgoingEdgeMap: ListMultimap[MutableVertex, MutableEdgeImpl] = ArrayListMultimap.create()

  private def checkInvariants() {
    require(edges flatMap (_.vertices) forall vertices_.contains)
    require(incomingEdgeMap.keySet forall vertices_.contains)
    require(incomingEdgeMap.values.toSet == edges_.toSet)
    require(outgoingEdgeMap.keySet forall vertices_.contains)
    require(outgoingEdgeMap.values.toSet == edges_.toSet)
    require(edges_ forall { edge ⇒ incomingEdgeMap.get(edge.target).contains(edge) && outgoingEdgeMap.get(edge.source).contains(edge) })
  }

  def vertices: List[MutableVertex] = vertices_

  def edges: List[MutableEdge] = edges_

  def incomingEdges(v: Vertex): List[MutableEdge] = v match {
    case mv: MutableVertexImpl ⇒ incomingEdgeMap.get(mv).toSeq.toList
    case _                     ⇒ Nil
  }

  def outgoingEdges(v: Vertex): List[MutableEdge] = v match {
    case mv: MutableVertexImpl ⇒ outgoingEdgeMap.get(mv).toSeq.toList
    case _                     ⇒ Nil
  }

  def copy = MutableGraphImpl.copy(this)

  def addVertex(name: Name): MutableVertex = {
    val v = new MutableVertexImpl(name)
    vertices_ = vertices_ :+ v
    checkInvariants()
    v
  }

  def removeVertex(v: MutableVertex) {
    vertices_ = vertices_ filterNot (_ == v)
    edges_ = edges_ filterNot (_ isIncidentTo v)
    incomingEdgeMap.removeAll(v)
    outgoingEdgeMap.removeAll(v)
    checkInvariants()
  }

  def addEdge(source: MutableVertex, target: MutableVertex, nameOpt: Option[Name] = None): MutableEdge = {
    val edge = new MutableEdgeImpl(source, target, nameOpt)
    edges_ = edges_ :+ edge
    incomingEdgeMap.put(target, edge)
    outgoingEdgeMap.put(source, edge)
    checkInvariants()
    edge
  }

  def removeEdge(edge: MutableEdge) {
    edges_ = edges_ filterNot (_ == edge)
    outgoingEdgeMap.get(edge.source).remove(edge)
    incomingEdgeMap.get(edge.target).remove(edge)
    checkInvariants()
  }

  private class MutableVertexImpl(initialName: Name) extends MutableVertex {

    private var name_ : Name = initialName

    def name = name_

    def setName(name: Name) { name_ = name }

    private var attributes_ : Map[Name, AnyRef] = Map()

    def attributes = attributes_

    def setAttribute(name: Name, value: AnyRef) {
      attributes_ += (name -> value)
    }

    def removeAttribute(name: Name) {
      attributes_ = attributes_ - name
    }

    override def toString = "[" + name + ": " + attributes + "]"

  }

  private class MutableEdgeImpl(initialSource: MutableVertex, initialTarget: MutableVertex, initialName: Option[Name] = None) extends MutableEdge {

    private var nameOpt_ : Option[Name] = initialName

    def nameOpt = nameOpt_

    def setName(name: Name) { nameOpt_ = Some(name) }

    private var source_ : MutableVertex = initialSource

    def source = source_

    private var target_ : MutableVertex = initialTarget

    def target = target_

    override def toString = "e(" + nameOpt + ", " + source + " -> " + target + ")"

  }

  override def toString = {
    var sb = new StringBuilder
    sb.append("MutableGraphImpl(\n")
    for (vertex ← vertices_)
      sb.append("  " + vertex + "\n")
    for (edge ← edges)
      sb.append("  " + edge + "\n")
    sb.append(")")
    sb.toString

  }

}
