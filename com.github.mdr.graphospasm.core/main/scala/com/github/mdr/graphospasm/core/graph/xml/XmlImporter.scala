package com.github.mdr.graphospasm.core
package graph.xml

import graph._
import graph.mutable._

import scala.xml._

object XmlImporter {

  object XSD {

    val int = "http://www.w3.org/2001/XMLSchema#int"
    val boolean = "http://www.w3.org/2001/XMLSchema#boolean"

  }

}

class XmlImporter(importSpec: XmlImportSpec) {

  import XmlImporter._

  private def includeElement(name: Name) =
    if (importSpec.blackList)
      !(importSpec.excludeElements.exists(_.exclude(name)))
    else
      importSpec.includes.exists(_.includeElement(name))

  private def includeAttribute(elemName: Name, attributeName: Name) =
    if (importSpec.blackList)
      !(importSpec.excludeAttributes.exists(_.exclude(elemName, attributeName)))
    else
      importSpec.includes.exists(_.includeAttribute(elemName, attributeName))

  private def rewriteElement(name: Name): Name =
    importSpec.renames.find(_.incomingLocation.matchesElement(name)).map(_.newName).getOrElse(name)

  private def rewriteAttribute(elementName: Name, attributeName: Name): Name =
    importSpec.renames.find(_.incomingLocation.matchesAttribute(elementName, attributeName)).map(_.newName).getOrElse(attributeName)

  private def coerceElement(elementName: Name, literal: String): AnyRef =
    importSpec.assignTypes
      .find(_.incomingLocation.matchesElement(elementName))
      .map { assignType ⇒ coerce(literal, assignType.typeName) }
      .getOrElse(literal)

  private def coerceAttributeValue(elementName: Name, attributeName: Name, literal: String): AnyRef =
    importSpec.assignTypes
      .find(_.incomingLocation.matchesAttribute(elementName, attributeName))
      .map { assignType ⇒ coerce(literal, assignType.typeName) }
      .getOrElse(literal)

  private def coerce(attributeValue: String, typeName: String): AnyRef = typeName match {
    case XSD.int     ⇒ java.lang.Integer.valueOf(attributeValue)
    case XSD.boolean ⇒ java.lang.Boolean.valueOf(attributeValue)
    case _           ⇒ attributeValue
  }

  private case class Attribute(name: Name, value: String)

  private def getAttributes(elem: Elem): List[Attribute] =
    elem.attributes.toList map { metadata ⇒
      val attributeName = Name(metadata.key, Option(metadata getNamespace elem) getOrElse "")
      val attributeValue = metadata.value(0).text
      Attribute(attributeName, attributeValue)
    }

  def makeGraph(root: Elem): MutableGraph = {

    val graph = MutableGraphImpl.emptyGraph

    def getName(elem: Elem) = Name(elem.label, Option(elem.namespace) getOrElse "")

    def visitElem(elem: Elem): Option[MutableVertex] = {
      val elementName = getName(elem)

      if (includeElement(elementName)) {
        val vertex = graph.addVertex(rewriteElement(elementName))
        for (Attribute(attributeName, attributeValue) ← getAttributes(elem) if includeAttribute(elementName, attributeName))
          vertex.setAttribute(rewriteAttribute(elementName, attributeName), coerceAttributeValue(elementName, attributeName, attributeValue))
        for {
          child ← elem.child
          childElement ← Some(child) collect { case e: Elem ⇒ e }
        } childElement.child.toList match {
          case List(t: Text) ⇒
            val childElementName = getName(childElement)
            if (includeElement(childElementName))
              vertex.setAttribute(rewriteElement(childElementName), coerceElement(childElementName, t.text))
          case _ ⇒
            for (childVertex ← visitElem(childElement))
              graph.addEdge(vertex, childVertex)
        }
        Some(vertex)
      } else
        None
    }

    visitElem(root)

    graph
  }

}

sealed trait AttributeSelector {
  def apply(attributeName: Name): Boolean
}

case class Attributes(names: List[Name]) extends AttributeSelector {
  def apply(attributeName: Name) = names contains attributeName
}

case object NoAttributes extends AttributeSelector {
  def apply(attributeName: Name) = false
}

case object AllAttributes extends AttributeSelector {
  def apply(attributeName: Name) = true
}

case class IncomingLocation(elementName: Name, attributeName: Option[Name] = None) {

  def matchesElement(candidateElementName: Name) = attributeName.isEmpty && candidateElementName == elementName

  def matchesAttribute(candidateElementName: Name, candidateAttributeName: Name) =
    candidateElementName == elementName && attributeName.exists(_ == candidateAttributeName)

}

sealed trait ImportDirective

case class Include(elementName: Name, attributeSelector: AttributeSelector) extends ImportDirective {

  def includeElement(candidateElementName: Name) = candidateElementName == elementName

  def includeAttribute(candidateElementName: Name, attributeName: Name) = candidateElementName == elementName && attributeSelector(attributeName)

}

case class ExcludeAttribute(elementName: Name, attributeSelector: AttributeSelector) extends ImportDirective {

  def exclude(candidateElementName: Name, attributeName: Name) = candidateElementName == elementName && attributeSelector(attributeName)

}

case class ExcludeElement(elementName: Name) extends ImportDirective {

  def exclude(candidateElementName: Name) = candidateElementName == elementName

}

case class Rename(incomingLocation: IncomingLocation, newName: Name) extends ImportDirective
case class AssignType(incomingLocation: IncomingLocation, typeName: String) extends ImportDirective

case class XmlImportSpec(blackList: Boolean, directives: List[ImportDirective]) {
  lazy val includes = directives collect { case d: Include ⇒ d }
  lazy val excludeAttributes = directives collect { case d: ExcludeAttribute ⇒ d }
  lazy val excludeElements = directives collect { case d: ExcludeElement ⇒ d }
  lazy val renames = directives collect { case d: Rename ⇒ d }
  lazy val assignTypes = directives collect { case d: AssignType ⇒ d }
}

