package com.github.mdr.graphospasm.grapheditor

import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.core.graph.xml._

import com.github.mdr.graphospasm.grapheditor.part._
import com.github.mdr.graphospasm.grapheditor.model.{ Node, _ }

import org.eclipse.draw2d.FigureUtilities
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.graphics.GC
import java.util.ArrayList
import org.eclipse.draw2d.graph.DirectedGraphLayout
import org.eclipse.draw2d.graph.{ Node => Draw2DNode, Edge, DirectedGraph }
import org.eclipse.draw2d.geometry._
import org.eclipse.gef.ui.parts._
import org.eclipse.gef._
import org.eclipse.core.runtime._
import scala.xml._

object DummyDataGetter {

  def getDiagram = {
    val xml = XML.loadFile("/home/matt/corpus2/scalariform/scalariform/pom.xml")

    val pomNamespace = "http://maven.apache.org/POM/4.0.0"
    val xsiNamespace = "http://www.w3.org/2001/XMLSchema-instance"
    val schemaLocationAttribute = Name("schemaLocation", xsiNamespace)
    val project = Name("project", pomNamespace)
    val exclude = ExcludeAttribute(project, Attributes(List(schemaLocationAttribute)))
    val graph = new XmlImporter(XmlImportSpec(blackList = true, directives = List(exclude))).makeGraph(xml)

    val diagram = createDiagram(graph)
    autolayoutDiagram(diagram)
    diagram
  }

  private def createDiagram(graph: Graph): GraphDiagram = {
    val diagram = new GraphDiagram
    val font = new GC(new Shell).getFont
    val fontMetrics = FigureUtilities.getFontMetrics(font)
    var vertexToNode = Map[Vertex, Node]()

    for (vertex <- graph.vertices) {
      val node = new Node
      node.name = vertex.name.simpleName
      val attributes = vertex.attributes map { case (Name(simpleName, _), value) => (simpleName, value) }
      node.attributes = attributes
      val height = (fontMetrics.getHeight) * attributes.size + 30
      val widestAttribute = if (attributes.isEmpty) 0 else attributes.map {
        case (name, value) ⇒
          FigureUtilities.getTextExtents(name, font).width + FigureUtilities.getTextExtents(value.toString, font).width
      }.max

      val width = scala.math.max(FigureUtilities.getTextExtents(node.name, font).width, widestAttribute) + 85

      node.bounds = new Rectangle(150, 150, width, height)
      diagram.add(node)
      vertexToNode = vertexToNode + (vertex -> node)
    }
    for (edge <- graph.edges)
      Connection.connect(vertexToNode(edge.source), vertexToNode(edge.target))

    diagram
  }

  private def createDiagram(xml: Elem): GraphDiagram = {
    def childElems(elem: Elem) = elem.child.collect { case e: Elem ⇒ e }
    val diagram = new GraphDiagram
    val font = new GC(new Shell).getFont
    val fontMetrics = FigureUtilities.getFontMetrics(font)

    def makeNode(elem: Elem): Node = {

      val node = new Node
      node.name = elem.label
      var attributes = Map[String, Any]()
      for (childElem ← childElems(elem)) {
        if (childElems(childElem).isEmpty)
          attributes = attributes + (childElem.label -> childElem.text)
        else {
          val childNode = makeNode(childElem)
          Connection.connect(node, childNode)
        }
      }
      node.attributes = attributes
      val height = (fontMetrics.getHeight) * attributes.size + 30
      val widestAttribute = if (attributes.isEmpty) 0 else attributes.map {
        case (name, value) ⇒
          FigureUtilities.getTextExtents(name, font).width + FigureUtilities.getTextExtents(value.toString, font).width
      }.max

      val width = scala.math.max(FigureUtilities.getTextExtents(node.name, font).width, widestAttribute) + 85

      node.bounds = new Rectangle(150, 150, width, height)
      diagram.add(node)
      node
    }

    makeNode(xml)
    diagram
  }
  private def autolayoutDiagram(diagram: com.github.mdr.graphospasm.grapheditor.model.GraphDiagram): Unit = {

    val directedGraphLayout = new DirectedGraphLayout
    val graph = new DirectedGraph

    var thingToNodeMap: Map[Node, Draw2DNode] = Map()
    var connections: Set[Connection] = Set()
    for (thing ← diagram.nodes) {
      val node = new Draw2DNode(thing)
      node.width = thing.width
      node.height = thing.height
      node.setPadding(new Insets(4, 4, 4, 4))
      thingToNodeMap = thingToNodeMap + (thing -> node)
      graph.nodes.asInstanceOf[ArrayList[Draw2DNode]].add(node)
      connections = connections ++ thing.allConnections
    }
    for {
      connection ← connections
      source = connection.source
      target = connection.target
    } {
      val sourceNode = thingToNodeMap(source)
      val targetNode = thingToNodeMap(target)
      val edge = new Edge(connection, sourceNode, targetNode)
      graph.edges.asInstanceOf[ArrayList[Edge]].add(edge)
    }

    directedGraphLayout.visit(graph)
    for ((thing, node) ← thingToNodeMap) {
      val bounds = new Rectangle(node.x + 2, node.y + 2, node.width, thing.height)
      thing.bounds = bounds
    }
  }

}