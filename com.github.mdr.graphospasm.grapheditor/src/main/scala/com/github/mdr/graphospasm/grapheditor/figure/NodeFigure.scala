package com.github.mdr.graphospasm.grapheditor.figure

import org.eclipse.swt.graphics.Color
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.graphics.Pattern
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d._

class NodeFigure extends Figure {

  setLayoutManager(new XYLayout)
  // setBorder(new LineBorder(1))

  //   setOpaque(true)

  //  val connectionAnchor = new ChopboxAnchor(this)

  private var name_ : String = ""

  def name_=(s: String) {
    name_ = s
    repaint()
  }

  def name = name_

  private var attributes_ : Map[String, Any] = Map()

  def attributes_=(atts: Map[String, Any]) {
    attributes_ = atts
    repaint()
  }
  def attributes = attributes_

  private final val TITLE_OFFSET = new Dimension(3, 3)

  override def paintFigure(g: Graphics) {
    super.paintFigure(g)
    val shadowSize = 6
    val bounds = getBounds.getCopy.resize(-shadowSize, -shadowSize)
    val display = Display.getDefault
    g.setBackgroundColor(new Color(null, 192, 192, 192))
    g.fillRoundRectangle(new Rectangle(bounds.x + shadowSize, bounds.y + shadowSize, bounds.width - 1, bounds.height - 1), 10, 10)

    g.setBackgroundColor(ColorConstants.white)
    g.fillRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1), 10, 10)
    val gradHeight = 30
    val pattern = new Pattern(display, bounds.x, bounds.y, bounds.x, bounds.y + gradHeight, new Color(null, 192, 192, 255), ColorConstants.white)
    g.setBackgroundPattern(pattern)
    //    g.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height)
    g.fillRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - 1, gradHeight), 10, 10)
    g.setBackgroundPattern(null)
    pattern.dispose()
    g.setForegroundColor(ColorConstants.black)
    g.drawRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1), 10, 10)
    g.setForegroundColor(ColorConstants.black)

    val nameDimension = FigureUtilities.getTextExtents(name, g.getFont)
    val titleTextPos = bounds.getTopLeft.getTranslated(new Dimension((bounds.width - nameDimension.width) / 2, 3))
    g.drawText(name, titleTextPos)
    val lineY = titleTextPos.y + nameDimension.height + 2
    if (attributes_.nonEmpty)
      g.drawLine(bounds.x, lineY, bounds.getRight.x, lineY)

    var currentY = lineY - bounds.y + 3

    val widestAttribute = if (attributes.isEmpty) 0 else attributes.keys.map { FigureUtilities.getTextExtents(_, g.getFont).width }.max
    val fontHeight = FigureUtilities.getFontMetrics(g.getFont).getHeight
    for (attributeName ← attributes_.keys.toSeq.sorted) {
      val attributeValue = attributes_(attributeName)
      g.drawText(attributeName, bounds.getTopLeft.getTranslated(new Dimension(5, currentY)))
      g.drawText("" + attributeValue, bounds.getTopLeft.getTranslated(new Dimension(17 + widestAttribute, currentY)))

      currentY += fontHeight
    }
  }

  val connectionAnchor = new ChopboxAnchor(this)
}
