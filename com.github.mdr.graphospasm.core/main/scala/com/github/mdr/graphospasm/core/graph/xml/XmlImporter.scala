package com.github.mdr.graphospasm.core
package graph.xml

import graph._
import graph.mutable._

import scala.xml._

class XmlImporter {

  def makeGraph(root: Elem): MutableGraph = {

    val graph = MutableGraphImpl.emptyGraph

    def getName(elem: Elem) = Name(elem.label, Option(elem.namespace) getOrElse "")

    def visitElem(elem: Elem): MutableVertex = {
      val vertex = graph.addVertex(getName(elem))
      for (metadata <- elem.attributes) {
        val attributeName = Name(metadata.key, Option(metadata getNamespace elem) getOrElse "")
        val attributeValue = metadata.value(0).text
        vertex.setAttribute(attributeName, attributeValue)
      }
      for {
        child <- elem.child
        childElem <- Some(child) collect { case e: Elem => e }
      } childElem.child.toList match {
        case List(t: Text) =>
          vertex.setAttribute(getName(childElem), t.text)
        case _ =>
          val childVertex = visitElem(childElem)
          graph.addEdge(vertex, childVertex)
      }
      vertex
    }

    visitElem(root)

    graph
  }

}