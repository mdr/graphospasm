package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.figure.NodeNameFigure
import org.eclipse.swt.widgets.Text
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.tools.CellEditorLocator

class RenameCellEditorLocator(figure: NodeNameFigure) extends CellEditorLocator {

  def relocate(celleditor: CellEditor) {
    val text = celleditor.getControl.asInstanceOf[Text]
    var rect = figure.getClientArea
    figure.translateToAbsolute(rect)
    val trim = text.computeTrim(0, 0, 0, 0)
    rect.translate(trim.x, trim.y)
    rect.width += trim.width
    rect.height += trim.height
    text.setBounds(rect.x, rect.y, rect.width, rect.height)
  }

}