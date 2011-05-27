package com.github.mdr.graphospasm.grapheditor.views

import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.part._
import org.eclipse.jface.viewers._
import org.eclipse.swt.graphics.Image
import org.eclipse.jface.action._
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.ui._
import org.eclipse.swt.widgets.Menu
import org.eclipse.swt.SWT

class NamespacesView extends ViewPart {

  private var viewer: TableViewer = _

  def createPartControl(parent: Composite) {
    viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL)
  }

  def setFocus() {
    viewer.getControl.setFocus
  }

}