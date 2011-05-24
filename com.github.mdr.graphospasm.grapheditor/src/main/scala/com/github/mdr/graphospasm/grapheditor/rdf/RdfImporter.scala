package com.github.mdr.graphospasm.grapheditor.rdf
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.util.FileManager
import com.hp.hpl.jena.rdf.model.Statement
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.rdf.model.Property
import com.hp.hpl.jena.rdf.model.RDFNode
import java.io.InputStream
import com.github.mdr.graphospasm.core.graph.mutable._
import com.github.mdr.graphospasm.core.graph._
import com.hp.hpl.jena.rdf.model.Literal

class RdfImporter {

  def read(inputStream: InputStream): Graph = {

    val graph = MutableGraphImpl.emptyGraph

    val model = ModelFactory.createDefaultModel()

    model.read(inputStream, "")

    val statementIterator = model.listStatements

    var resourceToVertexMap: Map[String, MutableVertex] = Map()

    while (statementIterator.hasNext()) {
      val stmt: Statement = statementIterator.nextStatement()

      val subject: Resource = stmt.getSubject
      val subjectURI = subject.getURI
      if (subjectURI != null) {
        if (!resourceToVertexMap.contains(subjectURI)) {
          val vertex = graph.addVertex(Name(subject.getLocalName, subject.getNameSpace))
          resourceToVertexMap += subjectURI -> vertex
        }
        val subjectVertex = resourceToVertexMap(subjectURI)
        val predicate: Property = stmt.getPredicate

        val predicateName = Name(predicate.getLocalName, predicate.getNameSpace)

        val obj: RDFNode = stmt.getObject

        obj match {
          case objResource: Resource ⇒
            val objectURI = objResource.getURI
            if (objectURI != null) {
              if (!resourceToVertexMap.contains(objectURI)) {
                val vertex = graph.addVertex(Name(objResource.getLocalName, objResource.getNameSpace))
                resourceToVertexMap += objectURI -> vertex
              }
              val objectVertex = resourceToVertexMap(objectURI)
              graph.addEdge(subjectVertex, objectVertex, Some(predicateName))
            }
          case objLiteral: Literal ⇒
            subjectVertex.setAttribute(predicateName, objLiteral.getValue)
          case _ ⇒
        }
      }
    }
    graph

  }
}