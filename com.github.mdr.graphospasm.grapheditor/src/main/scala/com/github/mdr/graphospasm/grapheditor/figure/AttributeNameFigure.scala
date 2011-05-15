package com.github.mdr.graphospasm.grapheditor.figure

import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d._

class AttributeNameFigure extends Label {

  private var attributeName_ : AttributeName = _

  def attributeName_=(attributeName: AttributeName) {
    attributeName_ = attributeName
    setText(attributeName.name.simpleName)
    repaint()
  }

  def attributeName = attributeName_

  override def paintFigure(g: Graphics) {
    g.setForegroundColor(ColorConstants.black)
    super.paintFigure(g)
  }

}
