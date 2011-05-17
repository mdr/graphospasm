package com.github.mdr.graphospasm.grapheditor.utils

import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.graphics.GC
import org.eclipse.swt.graphics.Font

object Utils {

  def withFont[T](p: Font ⇒ T): T = {
    val gc = new GC(new Shell)
    try
      p(gc.getFont)
    finally
      gc.dispose()
  }
}

