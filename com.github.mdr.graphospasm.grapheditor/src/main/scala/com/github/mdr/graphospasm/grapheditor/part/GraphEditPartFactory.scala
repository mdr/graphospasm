package com.github.mdr.graphospasm
package grapheditor.part

import grapheditor.model._

import org.eclipse.gef._

object GraphEditPartFactory extends EditPartFactory {

  def createEditPart(context: EditPart, model: Object): EditPart =
    model match {
      case graphDiagram: GraphDiagram ⇒ new GraphDiagramEditPart(graphDiagram)
      case node: Node                 ⇒ new NodeEditPart(node)
      case connection: Connection     ⇒ new ConnectionEditPart(connection)
    }

}