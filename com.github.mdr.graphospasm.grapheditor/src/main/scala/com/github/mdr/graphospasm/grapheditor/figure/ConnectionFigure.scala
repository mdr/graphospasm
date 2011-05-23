package com.github.mdr.graphospasm.grapheditor.figure
import org.eclipse.draw2d.Label
import org.eclipse.draw2d.PolygonDecoration
import org.eclipse.draw2d.PolylineConnection
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx

class ConnectionFigure extends PolylineConnectionEx {

  setLineWidth(2)
  //  setRoundedBendpointsRadius(12)
  //  setJumpLinks(true)
  setSmoothness(PolylineConnectionEx.SMOOTH_NORMAL)

  {
    val decoration = new PolygonDecoration
    decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP)
    setTargetDecoration(decoration)
  }
  //  val label = new Label("middle")
  //   add(label, new ConnectionLocator(this, ConnectionLocator.MIDDLE))
}

