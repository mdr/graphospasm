package com.github.mdr.graphospasm.grapheditor.figure

import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d._
import com.github.mdr.graphospasm.grapheditor.Plugin

class AttributeValueFigure extends AbstractNameFigure {

  setLabelAlignment(PositionConstants.LEFT)
  setTextPlacement(PositionConstants.WEST)

  private var isInteger_ = false

  def isInteger = isInteger_

  def isInteger_=(b: Boolean) {
    isInteger_ = b
    if (isInteger)
      setIcon(Plugin.int.createImage)
    else
      setIcon(null)
    repaint()
  }

}