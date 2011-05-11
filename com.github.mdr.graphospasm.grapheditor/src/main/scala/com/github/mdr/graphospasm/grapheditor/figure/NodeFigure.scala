package com.github.mdr.graphospasm.grapheditor.figure

import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d._

class NodeFigure extends Figure {

  setLayoutManager(new XYLayout)
  setBorder(new LineBorder(1))

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
    g.setForegroundColor(ColorConstants.black)
    val bounds = getBounds.getCopy
    val nameDimension = FigureUtilities.getTextExtents(name, g.getFont)
    val titleTextPos = bounds.getTopLeft.getTranslated(new Dimension((bounds.width - nameDimension.width) / 2, 3))
    g.drawText(name, titleTextPos)
    val lineY = titleTextPos.y + nameDimension.height + 2
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
