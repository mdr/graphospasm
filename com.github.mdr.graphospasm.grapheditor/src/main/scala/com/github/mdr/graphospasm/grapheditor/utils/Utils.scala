package com.github.mdr.graphospasm.grapheditor.utils

import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.graphics.GC
import org.eclipse.swt.graphics.Font

object Utils {

  private var font: Font = null

  def withFont[T](p: Font ⇒ T): T = {
    if (font == null) {
      val gc = new GC(new Shell)
      try
        font = gc.getFont
      finally
        gc.dispose()
    }
    p(font)
  }
}

