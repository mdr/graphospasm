package com.github.mdr.graphospasm.core.graph.test

import com.github.mdr.graphospasm.core.graph.mutable._
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.core.graph.xml._

object Test {

  def main(args: Array[String]) {

    val graph = MutableGraphImpl.emptyGraph
    val entrant = graph.addVertex(Name("Entrant"))
    entrant.setAttribute(Name("First name"), "Bob")
    println(entrant.attributes)

    val car = graph.addVertex(Name("Car"))
    graph.addEdge(entrant, car, Some(Name("has car")))

    val graph2 = graph.copy
    println(graph2.vertices(0).attributes)

    println(graph)
    val xml =
      <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <dependencies>
          <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.0</version>
            <type>jar</type>
            <scope>test</scope>
            <optional>true</optional>
          </dependency>
        </dependencies>
      </project>

    val pomNamespace = "http://maven.apache.org/POM/4.0.0"
    val project = Name("project", pomNamespace)
    val optional = Name("optional", pomNamespace)

    {
      val rename = Rename(IncomingLocation(project), Name("Project"))
      val optionalIsBoolean = AssignType(IncomingLocation(optional), XmlImporter.XSD.boolean)
      val importSpec = XmlImportSpec(blackList = true, directives = List(rename, optionalIsBoolean))
      val pomGraph = new XmlImporter(importSpec).makeGraph(xml)
      println(pomGraph)
    }

    {
      val include = Include(project, NoAttributes)
      val importSpec = XmlImportSpec(blackList = false, directives = List(include))
      val pomGraph = new XmlImporter(importSpec).makeGraph(xml)
      println(pomGraph)
    }
  }

}
