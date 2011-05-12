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
import org.eclipse.draw2d.graph.{ Node ⇒ Draw2DNode, Edge, DirectedGraph }
import org.eclipse.draw2d.geometry._
import org.eclipse.gef.ui.parts._
import org.eclipse.gef._
import org.eclipse.core.runtime._
import scala.xml._

object DummyDataGetter {

  def getDiagram = {
    val xml =
      <project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
        <modelVersion>4.0.0</modelVersion>
        <groupId>org.scalariform</groupId>
        <artifactId>scalariform</artifactId>
        <version>0.1.0-SNAPSHOT</version>
        <packaging>eclipse-plugin</packaging>
        <parent>
          <artifactId>scalariform.parent</artifactId>
          <groupId>org.scalariform</groupId>
          <version>0.1.0-SNAPSHOT</version>
        </parent>
        <dependencies>
          <dependency>
            <groupId>org.scalatest</groupId>
            <artifactId>scalatest</artifactId>
            <version>1.2-for-scala-2.8.0.final-SNAPSHOT</version>
            <scope>test</scope>
          </dependency>
        </dependencies>
        <build>
          <plugins>
            <plugin>
              <groupId>org.sonatype.tycho</groupId>
              <artifactId>maven-osgi-compiler-plugin</artifactId>
              <version>0.11</version>
              <configuration>
                <excludeResources>
                  <excludeResource>**/*.scala</excludeResource>
                </excludeResources>
              </configuration>
            </plugin>
          </plugins>
        </build>
      </project>

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

    for (vertex ← graph.vertices) {
      val node = new Node
      node.name = vertex.name
      val attributes = vertex.attributes map { case (Name(simpleName, _), value) ⇒ (simpleName, value) }
      node.attributes = attributes
      val height = (fontMetrics.getHeight) * attributes.size + 30
      val widestAttribute = if (attributes.isEmpty) 0 else attributes.map {
        case (name, value) ⇒
          FigureUtilities.getTextExtents(name, font).width + FigureUtilities.getTextExtents(value.toString, font).width
      }.max

      val width = scala.math.max(FigureUtilities.getTextExtents(node.name.simpleName, font).width, widestAttribute) + 85

      node.bounds = new Rectangle(150, 150, width, height)
      diagram.add(node)
      vertexToNode = vertexToNode + (vertex -> node)
    }
    for (edge ← graph.edges)
      Connection.connect(vertexToNode(edge.source), vertexToNode(edge.target))

    diagram
  }

  def createDiagram(xml: Elem): GraphDiagram = {
    def childElems(elem: Elem) = elem.child.collect { case e: Elem ⇒ e }
    val diagram = new GraphDiagram
    val font = new GC(new Shell).getFont
    val fontMetrics = FigureUtilities.getFontMetrics(font)

    def makeNode(elem: Elem): Node = {

      val node = new Node
      node.name = Name(elem.label, elem.namespace)
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
      val height = (fontMetrics.getHeight) * attributes.size + 38
      val widestAttribute = if (attributes.isEmpty) 0 else attributes.map {
        case (name, value) ⇒
          FigureUtilities.getTextExtents(name, font).width + FigureUtilities.getTextExtents(value.toString, font).width
      }.max

      val width = scala.math.max(FigureUtilities.getTextExtents(node.name.simpleName, font).width, widestAttribute) + 85

      node.bounds = new Rectangle(150, 150, width, height)
      diagram.add(node)
      node
    }

    makeNode(xml)
    diagram
  }
  def autolayoutDiagram(diagram: GraphDiagram) {

    val directedGraphLayout = new DirectedGraphLayout
    val graph = new DirectedGraph

    var thingToNodeMap: Map[Node, Draw2DNode] = Map()
    var connections: Set[Connection] = Set()
    for (node ← diagram.nodes) {
      val draw2dNode = new Draw2DNode(node)
      draw2dNode.width = node.width
      draw2dNode.height = node.height
      draw2dNode.setPadding(new Insets(8, 4, 8, 4))
      thingToNodeMap = thingToNodeMap + (node -> draw2dNode)
      graph.nodes.asInstanceOf[ArrayList[Draw2DNode]].add(draw2dNode)
      connections = connections ++ node.allConnections
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