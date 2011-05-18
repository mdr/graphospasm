package com.github.mdr.graphospasm.grapheditor.part

import org.eclipse.draw2d.IFigure
import com.github.mdr.graphospasm.grapheditor.figure._
import org.eclipse.swt.widgets.Text
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.tools.CellEditorLocator

class FixedRegionCellEditorLocator(figure: IFigure, location: Rectangle) extends CellEditorLocator {

  def relocate(cellEditor: CellEditor) {
    val workingLocation = location.getCopy
    figure.translateToAbsolute(workingLocation)
    val text = cellEditor.getControl.asInstanceOf[Text]
    val trim = text.computeTrim(0, 0, 0, 0)
    workingLocation.translate(trim.x, trim.y)
    workingLocation.resize(trim.width, trim.height)
    text.setBounds(workingLocation.x, workingLocation.y, workingLocation.width, workingLocation.height)
  }

}