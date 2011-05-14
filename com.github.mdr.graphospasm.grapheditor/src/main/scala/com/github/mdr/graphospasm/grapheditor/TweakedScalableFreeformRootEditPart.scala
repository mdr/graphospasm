package com.github.mdr.graphospasm.grapheditor

import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.ScalableFreeformLayeredPane
import org.eclipse.gef.LayerConstants
import org.eclipse.draw2d.FreeformLayer
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart

// Inject our own tweaked ScaledGraphics (probably won't need this in 3.7, see 
//  https://bugs.eclipse.org/bugs/show_bug.cgi?id=132361)
class TweakedScalableFreeformRootEditPart extends ScalableFreeformRootEditPart {
  class FeedbackLayer extends FreeformLayer {
    setEnabled(false)
  }

  override def createScaledLayers() = {
    val layers = new TweakedScalableFreeformLayeredPane()
    layers.add(createGridLayer(), LayerConstants.GRID_LAYER)
    layers.add(getPrintableLayers(), LayerConstants.PRINTABLE_LAYERS)
    layers.add(new FeedbackLayer(), LayerConstants.SCALED_FEEDBACK_LAYER)
    layers
  }

}

class TweakedScalableFreeformLayeredPane extends ScalableFreeformLayeredPane {
  override def paintClientArea(graphics: Graphics) {
    if (getChildren().isEmpty())
      return
    if (getScale == 1.0)
      super.paintClientArea(graphics)
    else {
      val g = new ScaledGraphics(graphics)
      val optimizeClip = getBorder == null || getBorder.isOpaque
      if (!optimizeClip)
        g.clipRect(getBounds.getCropped(getInsets))
      g.scale(getScale)
      g.pushState()
      paintChildren(g)
      g.dispose()
      graphics.restoreState()
    }
  }
}