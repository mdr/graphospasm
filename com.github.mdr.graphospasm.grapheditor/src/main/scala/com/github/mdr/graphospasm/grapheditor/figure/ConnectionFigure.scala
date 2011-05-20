package com.github.mdr.graphospasm.grapheditor.figure
import org.eclipse.draw2d.Label
import org.eclipse.draw2d.PolygonDecoration
import org.eclipse.draw2d.PolylineConnection

class ConnectionFigure extends PolylineConnection {

  setLineWidth(2)

  {
    val decoration = new PolygonDecoration
    decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP)
    setTargetDecoration(decoration)
  }
  //  val label = new Label("middle")
  //   add(label, new ConnectionLocator(this, ConnectionLocator.MIDDLE))
}

