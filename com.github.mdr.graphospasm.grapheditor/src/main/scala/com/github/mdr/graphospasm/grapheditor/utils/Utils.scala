package com.github.mdr.graphospasm.grapheditor.utils

import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.graphics.GC
import org.eclipse.swt.graphics.Font
import org.eclipse.gef.commands.Command
import org.eclipse.gef.commands.CompoundCommand
import scala.collection.mutable.WeakHashMap
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.draw2d.FigureUtilities
import org.eclipse.draw2d.geometry.Dimension

object Utils {

  private val textExtentsCache = new WeakHashMap[String, Dimension]

  def getTextExtents(s: String) =
    textExtentsCache.get(s) match {
      case None ⇒
        val extents = FigureUtilities.getTextExtents(s, getFont)
        textExtentsCache.put(s, extents)
        extents
      case Some(extents) ⇒ extents
    }

  private var font: Font = null

  private def getFont = {
    if (font == null) {
      val gc = new GC(new Shell)
      try
        font = gc.getFont
      finally
        gc.dispose()
    }
    font
  }

  def withFont[T](p: Font ⇒ T): T = p(getFont)

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

