package com.github.mdr.graphospasm
package core.graph.test

import core.graph.mutable._
import core.graph._
import core.graph.xml._

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
    val pomGraph = new XmlImporter().makeGraph(xml)

    println(pomGraph)

  }

}
