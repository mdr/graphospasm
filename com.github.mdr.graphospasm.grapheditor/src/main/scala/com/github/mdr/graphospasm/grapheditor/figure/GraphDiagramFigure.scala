package com.github.mdr.graphospasm.grapheditor.figure

import org.eclipse.draw2d.MarginBorder
import org.eclipse.draw2d.FreeformLayout
import org.eclipse.draw2d.FreeformLayer

class GraphDiagramFigure extends FreeformLayer {

  setLayoutManager(new FreeformLayout)
  setBorder(new MarginBorder(5))

}