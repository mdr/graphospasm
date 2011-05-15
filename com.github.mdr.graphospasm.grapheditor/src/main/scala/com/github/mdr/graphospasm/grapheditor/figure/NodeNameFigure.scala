package com.github.mdr.graphospasm.grapheditor.figure

import com.github.mdr.graphospasm.grapheditor.model.NodeName
import org.eclipse.swt.graphics.Color
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.graphics.Pattern
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d._

class NodeNameFigure extends Label {

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
