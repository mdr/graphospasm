package com.github.mdr.graphospasm.grapheditor

import com.github.mdr.graphospasm.core.graph.xml._
import com.github.mdr.graphospasm.core.graph._

import com.github.mdr.graphospasm.grapheditor.part._

import org.eclipse.swt.graphics.GC
import org.eclipse.gef.ui.parts._
import org.eclipse.gef._
import org.eclipse.core.runtime._
import scala.xml._

class GraphEditor extends GraphicalEditorWithFlyoutPalette {

  private final val editDomain = new DefaultEditDomain(this)
  setEditDomain(editDomain)

  override def getPaletteRoot = new GraphEditorPalette

  override def configureGraphicalViewer() {
    super.configureGraphicalViewer()
    val viewer = getGraphicalViewer.asInstanceOf[ScrollingGraphicalViewer]

    viewer.setEditPartFactory(GraphEditPartFactory)

    viewer.setContents(DummyDataGetter.getDiagram)
    editDomain.addViewer(viewer)

  }

  def doSave(monitor: IProgressMonitor) {
    //    val file = getEditorInput.asInstanceOf[IFileEditorInput].getFile
    //    file.setContents(new ByteArrayInputStream(modelAsText.getBytes), true, false, monitor)
    //    getCommandStack().markSaveLocation()
  }

}