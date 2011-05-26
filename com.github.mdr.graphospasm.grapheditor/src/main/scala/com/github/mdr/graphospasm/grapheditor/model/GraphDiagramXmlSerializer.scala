package com.github.mdr.graphospasm.grapheditor.model

import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d.geometry.Rectangle
import scala.xml.{ Node ⇒ XmlNode, _ }
import PartialFunction.condOpt

object GraphDiagramXmlSerializer {

  def serialize(graphDiagram: GraphDiagram): Elem = {
    var nextId = 1
    var nodeIds = Map[Node, Int]()

    def makeConnections(childNodes: List[Node]) =
      childNodes.flatMap(_.allConnections).distinct.map { connection ⇒
        val sourceId = nodeIds(connection.source).toString
        val targetId = nodeIds(connection.target).toString
        val connectionNameOpt = connection.nameOpt.map {
          case name ⇒ <name simpleName={ name.simpleName } namespace={ name.namespace }/>
        }.toList
        <edge source={ sourceId } target={ targetId }>
          { connectionNameOpt }
        </edge>
      }

    def makeAttribute(attributeName: AttributeName, attributeValue: AttributeValue): Elem = {
      val name = attributeName.name
      val type_ = attributeValue.value.getClass.getName
      <attribute simpleName={ name.simpleName } namespace={ name.namespace } type={ type_ }>{ attributeValue.value }</attribute>
    }

    def serializeNode(node: Node): Elem = {
      val nodeId = assignIdToNode(node)
      val location = node.bounds; import location._
      val nodeName = node.name
      val attributes = node.getAttributes.map { case (name, value) ⇒ makeAttribute(name, value) }
      <vertex id={ nodeId } x={ x.toString } y={ y.toString } width={ width.toString } height={ height.toString }>
        <name simpleName={ nodeName.simpleName } namespace={ nodeName.namespace }/>
        { attributes }
      </vertex>
    }

    def assignIdToNode(node: Node) = {
      val nodeId = nextId
      nextId += 1
      nodeIds = nodeIds + (node -> nodeId)
      nodeId.toString
    }

    def serialize(graphDiagram: GraphDiagram): Elem = {
      val childNodes = graphDiagram.nodes
      <graph>{ childNodes.map(serializeNode) ++ makeConnections(childNodes) }</graph>
    }
    serialize(graphDiagram)
  }

  def deserialize(elem: Elem): GraphDiagram = {

    var nodeIds: Map[Int, Node] = Map()

    def createConnections(elem: Elem, nodeIds: Map[Int, Node]) =
      elem \ "edge" foreach { edgeElem ⇒
        val source = nodeIds((edgeElem \ "@source").text.toInt)
        val target = nodeIds((edgeElem \ "@target").text.toInt)
        val connection = Connection.connect(source, target)
        edgeElem \ "name" foreach { nameElem ⇒
          connection.nameOpt = Some(getName(nameElem))
        }
      }

    def getLocation(nodeElem: XmlNode): Rectangle = {
      val x = (nodeElem \ "@x").text.toInt
      val y = (nodeElem \ "@y").text.toInt
      val width = (nodeElem \ "@width").text.toInt
      val height = (nodeElem \ "@height").text.toInt
      new Rectangle(x, y, width, height)
    }

    def getName(elem: XmlNode): Name = {
      val simpleName = (elem \ "@simpleName").text
      val namespace = (elem \ "@namespace").text
      Name(simpleName, namespace)
    }

    def getAttribute(elem: XmlNode): (Name, AnyRef) = {
      val name = getName(elem)
      val type_ = (elem \ "@type").text
      val value = if (type_ == classOf[java.lang.Integer].getName) {
        Integer.parseInt(elem.text).asInstanceOf[AnyRef]
      } else
        elem.text
      (name, value)
    }

    def deserializeNode(elem: XmlNode): Node = {
      val id = (elem \ "@id").text.toInt
      val name = getName((elem \ "name")(0))
      val node = new Node(name)
      elem \ "attribute" foreach { attributeElem ⇒
        val (name, value) = getAttribute(attributeElem)
        node.addAttribute(name, value)
      }
      node.bounds = getLocation(elem)
      nodeIds = nodeIds + (id -> node)
      node
    }

    val diagram = new GraphDiagram

    for (child ← elem.nonEmptyChildren) {
      condOpt(child) {
        case <vertex>{ _* }</vertex> ⇒ deserializeNode(child)
      } foreach diagram.add
    }
    createConnections(elem, nodeIds)

    diagram
  }

}

