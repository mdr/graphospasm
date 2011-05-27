package com.github.mdr.graphospasm.grapheditor.figure
import org.eclipse.draw2d.Label
import org.eclipse.draw2d.PolygonDecoration
import org.eclipse.draw2d.PolylineConnection
import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.ColorConstants
import org.eclipse.draw2d.ConnectionLocator
import org.eclipse.draw2d.LineBorder
import org.eclipse.draw2d.AbstractBorder
import org.eclipse.draw2d.IFigure
import org.eclipse.draw2d.geometry.Insets
import org.eclipse.draw2d.CompoundBorder
import com.github.mdr.graphospasm.grapheditor.Plugin

class EmptyBorder extends AbstractBorder {
  def getInsets(figure: IFigure) = new Insets(getWidth)

  def getWidth = 3

  override val isOpaque = false

  def paint(figure: IFigure, graphics: Graphics, insets: Insets) {

  }

}

class ConnectionFigure extends PolylineConnection {

  private val toolTipLabel = new Label

  setToolTip(toolTipLabel)

  private var toolTipText_ : String = ""

  def toolTipText_=(toolTipText: String) {
    toolTipText_ = toolTipText
    toolTipLabel.setText(toolTipText)
    repaint()
  }

  def toolTipText = toolTipText_

  private var targetFeedback_ = false

  def targetFeedback = targetFeedback_

  def targetFeedback_=(b: Boolean) {
    targetFeedback_ = b
    if (targetFeedback_)
      setForegroundColor(Plugin.backgroundBlue)
    else
      setForegroundColor(ColorConstants.black)
    repaint()
  }

  setLineWidth(2)
  setForegroundColor(ColorConstants.black)
  //  setRoundedBendpointsRadius(12)
  //  setJumpLinks(true)
  // setSmoothness(PolylineConnectionEx.SMOOTH_NORMAL)

  {
    val decoration = new PolygonDecoration
    decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP)
    setTargetDecoration(decoration)
  }

  def setLabel(s: String) {
    label.setText(s)
    label.setVisible(true)
    repaint()
  }

  def noLabel() {
    label.setText("")
    label.setVisible(false)
    repaint()
  }

  val label = new Label("")
  noLabel()

  {
    add(label, new ConnectionLocator(this, ConnectionLocator.MIDDLE))
    label.setOpaque(true)
    label.setBackgroundColor(ColorConstants.buttonLightest)
    val border = new CompoundBorder(new LineBorder, new EmptyBorder)
    label.setBorder(border)
  }

  override def paintFigure(g: Graphics) {
    // g.setForegroundColor(ColorConstants.black)
    super.paintFigure(g)
  }
}

