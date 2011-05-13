package com.github.mdr.graphospasm.grapheditor.figure

import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d._

class AttributeValueFigure extends Label {

  private var attributeValue_ : AttributeValue = _

  def attributeValue_=(attributeValue: AttributeValue) {
    attributeValue_ = attributeValue
    setText(attributeValue.value.toString)
    repaint()
  }

  def attributeValue = attributeValue_

}
