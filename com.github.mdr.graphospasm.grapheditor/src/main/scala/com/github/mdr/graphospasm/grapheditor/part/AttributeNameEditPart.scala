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

class AttributeNameEditPart(attributeName: AttributeName) extends AbstractGraphicalEditPart with Listener {

  setModel(attributeName)

  override def getFigure = super.getFigure.asInstanceOf[AttributeNameFigure]
  override def getParent = super.getParent.asInstanceOf[NodeEditPart]
  override def getModel = super.getModel.asInstanceOf[AttributeName]
  override def createFigure = new NodeNameFigure

  protected def createEditPolicies() {
  }

  override def refreshVisuals() {
    getFigure.attributeName = attributeName
    getParent.setLayoutConstraint(this)
  }

  override def activate() {
    if (!isActive) attributeName.addListener(this)
    super.activate()
  }

  override def deactivate() {
    if (isActive) attributeName.removeListener(this)
    super.deactivate()
  }

  def changed(event: Event) {
    refreshVisuals()
  }

}
