package com.github.mdr.graphospasm.grapheditor.figure

import com.github.mdr.graphospasm.grapheditor.model.NodeName
import org.eclipse.swt.graphics.Color
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.graphics.Pattern
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d._

class NodeNameFigure extends Figure {

  setLayoutManager(new XYLayout)

  private var name_ : NodeName = _

  def name_=(nodeName: NodeName) {
    name_ = nodeName
    repaint()
  }

  def name = name_

  override def paintFigure(g: Graphics) {
    super.paintFigure(g)

    val nameDimension = FigureUtilities.getTextExtents(name.name.simpleName, g.getFont)
    val titleTextPos = bounds.getTopLeft.getTranslated(new Dimension((bounds.width - nameDimension.width) / 2, 3))
    g.drawText(name.name.simpleName, titleTextPos)
  }

}
