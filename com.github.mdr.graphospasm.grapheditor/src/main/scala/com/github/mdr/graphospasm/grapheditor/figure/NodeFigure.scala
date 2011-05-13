package com.github.mdr.graphospasm.grapheditor.figure

import org.eclipse.swt.graphics.Color
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.graphics.Pattern
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d._
import scala.math.{ max, min }
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

  def getContentArea(bounds: Rectangle) = bounds.getCopy.resize(-shadowSize, -shadowSize)

  val shadowSize = 6
  override def paintFigure(g: Graphics) {
    super.paintFigure(g)
    val contentArea = getContentArea(getBounds)
    val display = Display.getDefault
    g.setBackgroundColor(new Color(null, 192, 192, 192))
    g.fillRoundRectangle(new Rectangle(contentArea.x + shadowSize, contentArea.y + shadowSize, contentArea.width - 1, contentArea.height - 1), 10, 10)

    g.setBackgroundColor(ColorConstants.white)
    g.fillRoundRectangle(new Rectangle(contentArea.x, contentArea.y, contentArea.width - 1, contentArea.height - 1), 10, 10)
    val gradHeight = 30
    val pattern = new Pattern(display, contentArea.x, contentArea.y, contentArea.x, contentArea.y + gradHeight, new Color(null, 192, 192, 255), ColorConstants.white)
    g.setBackgroundPattern(pattern)
    //    g.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height)
    g.fillRoundRectangle(new Rectangle(contentArea.x, contentArea.y, contentArea.width - 1, gradHeight), 10, 10)
    g.setBackgroundPattern(null)
    pattern.dispose()
    g.setForegroundColor(ColorConstants.black)
    g.drawRoundRectangle(new Rectangle(contentArea.x, contentArea.y, contentArea.width - 1, contentArea.height - 1), 10, 10)
    g.setForegroundColor(ColorConstants.black)

    val nameDimension = FigureUtilities.getTextExtents(name, g.getFont)
    val titleTextPos = contentArea.getTopLeft.getTranslated(new Dimension((contentArea.width - nameDimension.width) / 2, 3))
    //    g.drawText(name, titleTextPos)
    val lineY = titleTextPos.y + nameDimension.height + 2
    if (attributes_.nonEmpty)
      g.drawLine(contentArea.x, lineY, contentArea.getRight.x - 1, lineY)

    var currentY = lineY - contentArea.y + 3

    val widestAttribute = if (attributes.isEmpty) 0 else attributes.keys.map { FigureUtilities.getTextExtents(_, g.getFont).width }.max
    val fontHeight = FigureUtilities.getFontMetrics(g.getFont).getHeight
    for (attributeName ← attributes_.keys.toSeq.sorted) {
      val attributeValue = attributes_(attributeName)
      g.drawText(attributeName, contentArea.getTopLeft.getTranslated(new Dimension(5, currentY)))
      g.drawText("" + attributeValue, contentArea.getTopLeft.getTranslated(new Dimension(17 + widestAttribute, currentY)))

      currentY += fontHeight
    }
  }

  val connectionAnchor = new ChopboxAnchor(this)
}
