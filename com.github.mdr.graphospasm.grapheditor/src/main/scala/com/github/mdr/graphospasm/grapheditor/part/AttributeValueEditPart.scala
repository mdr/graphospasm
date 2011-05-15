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

class AttributeValueEditPart(val attributeValue: AttributeValue) extends NodeChildEditPart with Listener {

  setModel(attributeValue)

  override def getFigure = super.getFigure.asInstanceOf[AttributeValueFigure]
  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
  override def getModel = super.getModel.asInstanceOf[AttributeValue]
  override def createFigure = new AttributeValueFigure

  protected def createEditPolicies() {
  }

  override def refreshVisuals() {
    getFigure.name = attributeValue.value.toString
    getParent.relayout()
  }

  override def activate() {
    if (!isActive) attributeValue.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) attributeValue.removeListener(this)
    super.deactivate()
  }

  def changed(event: Event) {
    refreshVisuals()
  }

}
