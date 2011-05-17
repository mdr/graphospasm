package com.github.mdr.graphospasm.grapheditor.figure

import org.eclipse.draw2d.PositionConstants
import org.eclipse.draw2d.ColorConstants
import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.Label

class AbstractNameFigure extends Label {

  setLabelAlignment(PositionConstants.LEFT)

  private var name_ : String = _

  def name_=(name: String) {
    name_ = name
    setText(name)
    repaint()
  }

  def name = name_

  override def paintFigure(g: Graphics) {
    g.setForegroundColor(ColorConstants.black)
    super.paintFigure(g)
  }

}