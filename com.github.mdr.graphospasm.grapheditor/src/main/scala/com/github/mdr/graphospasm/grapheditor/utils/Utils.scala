package com.github.mdr.graphospasm.grapheditor.utils

import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.graphics.GC
import org.eclipse.swt.graphics.Font
import org.eclipse.gef.commands.Command
import org.eclipse.gef.commands.CompoundCommand

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

  def time[T](s: String)(f: ⇒ T): T = {
    val start = System.currentTimeMillis
    val result = f
    val duration = System.currentTimeMillis - start
    println(s + ": " + duration + "ms")
    result
  }

  def compoundCommand(commands: Seq[Command]) = {
    val compoundCommand = new CompoundCommand
    commands.foreach(compoundCommand.add)
    compoundCommand.unwrap
  }

}

