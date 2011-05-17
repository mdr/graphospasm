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

class AttributeNameEditPart(val attributeName: AttributeName) extends AbstractNameEditPart(attributeName) {

  override def getFigure = super.getFigure.asInstanceOf[AttributeNameFigure]
  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
  override def getModel = super.getModel.asInstanceOf[AttributeName]
  override def createFigure = new AttributeNameFigure

  override def createEditPolicies() {
    super.createEditPolicies()
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new AttributeNameComponentEditPolicy)
  }

}
