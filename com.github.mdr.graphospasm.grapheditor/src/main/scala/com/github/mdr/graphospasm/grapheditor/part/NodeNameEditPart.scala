package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import com.github.mdr.graphospasm.grapheditor.figure._

import org.eclipse.gef.editparts._
import org.eclipse.gef._
import org.eclipse.gef
import org.eclipse.draw2d._
import org.eclipse.draw2d.geometry._
import scala.collection.JavaConversions._
import java.util.{ List ⇒ JList }
import scala.collection.JavaConversions._

class NodeNameEditPart(nodeName: NodeName) extends AbstractNameEditPart(nodeName) {

  override def getFigure = super.getFigure.asInstanceOf[NodeNameFigure]
  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
  override def getModel = super.getModel.asInstanceOf[NodeName]
  override def createFigure = new NodeNameFigure

}
