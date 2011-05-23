package com.github.mdr.graphospasm.grapheditor.figure
import org.eclipse.draw2d.Label
import org.eclipse.draw2d.PolygonDecoration
import org.eclipse.draw2d.PolylineConnection
//import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx
import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.ColorConstants
import org.eclipse.draw2d.ConnectionLocator
import org.eclipse.draw2d.LineBorder

class ConnectionFigure extends PolylineConnection {

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

  {
    add(label, new ConnectionLocator(this, ConnectionLocator.MIDDLE))
    label.setOpaque(true)
    label.setBackgroundColor(ColorConstants.buttonLightest)
    label.setBorder(new LineBorder)
  }

  override def paintFigure(g: Graphics) {
    g.setForegroundColor(ColorConstants.black)
    super.paintFigure(g)
  }
}

