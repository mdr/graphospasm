package com.github.mdr.graphospasm.grapheditor.figure

import org.eclipse.draw2d.PositionConstants
import org.eclipse.draw2d.ColorConstants
import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.Label

class AbstractNameFigure extends Label {

  setLabelAlignment(PositionConstants.LEFT)

  private val toolTipLabel = new Label
  setToolTip(toolTipLabel)

  private var name_ : String = ""

  def name_=(name: String) {
    name_ = name
    setText(name)
    repaint()
  }

  def name = name_

  private var toolTipText_ : String = ""

  def toolTipText_=(toolTipText: String) {
    toolTipText_ = toolTipText
    toolTipLabel.setText(toolTipText)
    repaint()
  }

  def toolTipText = toolTipText_

  override def paintFigure(g: Graphics) {
    g.setForegroundColor(ColorConstants.black)
    super.paintFigure(g)
  }

}