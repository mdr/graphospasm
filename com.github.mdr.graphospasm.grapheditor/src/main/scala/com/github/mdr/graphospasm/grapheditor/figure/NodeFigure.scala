package com.github.mdr.graphospasm.grapheditor.figure

import com.github.mdr.graphospasm.grapheditor.model.NodeContentsLayouter
import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.grapheditor.Plugin
import com.github.mdr.graphospasm.grapheditor.ScaledGraphics
import org.eclipse.swt.graphics.Color
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.graphics.Pattern
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.{ ScaledGraphics ⇒ _, _ }
import scala.math.{ max, min }

object NodeFigure {

  private val shadowSize = 6
  private val shadowColour = new Color(null, 192, 192, 192)
  private val TITLE_OFFSET = new Dimension(3, 3)
  private val gradientColour = new Color(null, 192, 192, 255)
  private val gradientHeight = 30
}

class NodeFigure extends Figure {

  import NodeFigure._

  setLayoutManager(new XYLayout)

  private var targetFeedback_ = false

  def targetFeedback = targetFeedback_

  def targetFeedback_=(b: Boolean) {
    targetFeedback_ = b
    repaint()
  }

  private var name_ : String = ""

  def name_=(s: String) {
    name_ = s
    nameLabel.setText(s)
    repaint()
  }

  def name = name_

  private var nameBounds_ : Rectangle = new Rectangle(0, 0, 0, 0)

  def nameBounds = nameBounds_

  def nameBounds_=(bounds: Rectangle) {
    nameBounds_ = bounds
    setConstraint(nameLabel, bounds)
    invalidate()
    repaint()
  }

  val nameLabel = new Label(name) {
    override def paintFigure(g: Graphics) {
      g.setForegroundColor(ColorConstants.black)
      super.paintFigure(g)
    }
  }

  add(nameLabel, bounds)

  private final var hasAttributes_ = false

  def hasAttributes_=(b: Boolean) {
    hasAttributes_ = b
  }

  def hasAttributes = hasAttributes_

  def getContentArea(bounds: Rectangle) = bounds.getCopy.resize(-shadowSize, -shadowSize)

  override def paintFigure(g: Graphics) {
    super.paintFigure(g)
    g.pushState()
    val contentArea = getContentArea(getBounds)
    val display = Display.getDefault

    // Shadow
    g.setBackgroundColor(shadowColour)
    g.fillRoundRectangle(new Rectangle(contentArea.x + shadowSize, contentArea.y + shadowSize, contentArea.width - 1, contentArea.height - 1), 10, 10)

    // Background
    val backgroundColour = if (targetFeedback) Plugin.backgroundBlue else ColorConstants.white
    g.setBackgroundColor(backgroundColour)
    g.fillRoundRectangle(new Rectangle(contentArea.x, contentArea.y, contentArea.width - 1, contentArea.height - 1), 10, 10)

    // Gradient
    g.pushState()
    val pattern = new Pattern(display, contentArea.x, contentArea.y, contentArea.x, contentArea.y + gradientHeight, gradientColour, backgroundColour)
    if (g.isInstanceOf[ScaledGraphics])
      g.asInstanceOf[ScaledGraphics].setBackgroundPattern(display, contentArea.x, contentArea.y, contentArea.x, contentArea.y + gradientHeight, gradientColour, backgroundColour)
    else
      g.setBackgroundPattern(pattern)
    g.fillRoundRectangle(new Rectangle(contentArea.x, contentArea.y, contentArea.width - 1, gradientHeight - 3 /* <= adjusted for a couple of glitches */ ), 10, 10)
    pattern.dispose()
    g.setBackgroundPattern(null)
    g.popState()

    // Border
    g.setForegroundColor(ColorConstants.black)
    g.drawRoundRectangle(new Rectangle(contentArea.x, contentArea.y, contentArea.width - 1, contentArea.height - 1), 10, 10)

    // Name - attribute divider line
    val titleTextPos = contentArea.getTopLeft.getTranslated(new Dimension((contentArea.width - nameBounds.width) / 2, 3))
    val lineY = titleTextPos.y + nameBounds.height + 2
    if (hasAttributes_)
      g.drawLine(contentArea.x, lineY, contentArea.getRight.x - 1, lineY)
    g.popState()
  }

  val connectionAnchor = new ChopboxAnchor(this)
}
