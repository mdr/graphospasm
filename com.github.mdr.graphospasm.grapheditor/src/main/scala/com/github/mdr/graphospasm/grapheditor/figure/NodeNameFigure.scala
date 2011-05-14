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

  private var nodeName_ : NodeName = _

  def nodeName_=(nodeName: NodeName) {
    nodeName_ = nodeName
    setText(nodeName.name.simpleName)
    repaint()
  }

  def nodeName = nodeName_
  override def paintFigure(g: Graphics) {
    g.setForegroundColor(ColorConstants.black)
    super.paintFigure(g)
  }
}
