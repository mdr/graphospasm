package com.github.mdr.graphospasm.grapheditor.part

import com.github.mdr.graphospasm.grapheditor.figure._
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Font
import org.eclipse.swt.graphics.FontData
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Text

import org.eclipse.jface.action.IAction
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.jface.viewers.TextCellEditor
import org.eclipse.ui.IActionBars
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.actions.ActionFactory
import org.eclipse.ui.part.CellEditorActionHandler

import org.eclipse.gef.GraphicalEditPart
import org.eclipse.gef.editparts.ZoomListener
import org.eclipse.gef.editparts.ZoomManager
import org.eclipse.gef.tools.CellEditorLocator
import org.eclipse.gef.tools.DirectEditManager

class RenameNodeNameEditManager(source: GraphicalEditPart, locator: CellEditorLocator) extends DirectEditManager(source, null, locator) {

  override def createCellEditorOn(composite: Composite) = new TextCellEditor(composite /*, SWT.MULTI | SWT.WRAP */ )

  override def initCellEditor() {
    val figure = getEditPart.getFigure.asInstanceOf[NodeFigure]
    getCellEditor.setValue(figure.name)
  }

}