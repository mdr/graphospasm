package com.github.mdr.graphospasm.grapheditor.model

import com.github.mdr.graphospasm.core.graph.Name
import org.eclipse.draw2d.geometry._

class Node(initialName: Name) extends Observable {

  private var name_ : Name = initialName

  def name = name_

  def name_=(newName: Name) {
    name_ = newName
    fireEvent(LocalPropertyChanged)
  }

  def getAttributes = attributeNameValues

  private var attributeNameValues: List[(AttributeName, AttributeValue)] = Nil

  def removeAttribute(attributeName: AttributeName) {
    attributeNameValues = attributeNameValues.filterNot(_._1 == attributeName)
    fireEvent(LocalPropertyChanged)
  }

  def addAttribute(name: Name, value: AnyRef): (AttributeName, AttributeValue) = {
    require(!attributeNameValues.exists(_._1.name == name))
    val attributeName = new AttributeName(name)
    val attributeValue = new AttributeValue(value)
    addAttribute(attributeName, attributeValue)
  }

  def addAttribute(attributeName: AttributeName, attributeValue: AttributeValue): (AttributeName, AttributeValue) = {
    attributeNameValues = attributeNameValues :+ (attributeName, attributeValue)
    fireEvent(LocalPropertyChanged)
    (attributeName, attributeValue)
  }

  private var attributes_ : Map[String, AnyRef] = Map()

  private var sourceConnections_ : List[Connection] = Nil

  private var targetConnections_ : List[Connection] = Nil

  private var bounds_ = new Rectangle(0, 0, 0, 0)

  private var diagram_ : GraphDiagram = _

  def attributes_=(atts: Map[String, AnyRef]) { attributes_ = atts }
  def attributes = attributes_

  def sourceConnections: List[Connection] = sourceConnections_

  def targetConnections: List[Connection] = targetConnections_

  def allConnections = sourceConnections_ ++ targetConnections_

  private[model] def addSourceConnection(connection: Connection) {
    sourceConnections_ = sourceConnections_ :+ connection
    fireEvent(LocalPropertyChanged)
  }

  private[model] def removeSourceConnection(connection: Connection) {
    sourceConnections_ = sourceConnections_ filterNot (_ == connection)
    fireEvent(LocalPropertyChanged)
  }

  private[model] def addTargetConnection(connection: Connection) {
    targetConnections_ = targetConnections_ :+ connection
    fireEvent(LocalPropertyChanged)
  }

  private[model] def removeTargetConnection(connection: Connection) {
    targetConnections_ = targetConnections_ filterNot (_ == connection)
    fireEvent(LocalPropertyChanged)
  }

  def isSource = targetConnections_.isEmpty

  def diagram = diagram_

  def setDiagram(diagram: GraphDiagram) { diagram_ = diagram }

  def unsetDiagram() { diagram_ = null }

  def bounds = bounds_.getCopy

  def bounds_=(newBounds: Rectangle) {
    bounds_ = newBounds.getCopy
    fireEvent(LocalPropertyChanged)
  }

  def width = size.width
  def height = size.height
  def x = bounds_.x
  def y = bounds_.y

  def size = bounds.getSize

  def size_=(dimension: Dimension) {
    bounds_ = bounds_.getCopy.setSize(dimension)
    fireEvent(LocalPropertyChanged)
  }

}

