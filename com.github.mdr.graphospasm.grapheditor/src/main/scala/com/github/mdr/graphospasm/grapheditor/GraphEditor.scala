package com.github.mdr.graphospasm.grapheditor

import org.eclipse.swt.SWT
import org.eclipse.gef.editparts.ScalableRootEditPart
import org.eclipse.draw2d.FanRouter
import org.eclipse.draw2d.ShortestPathConnectionRouter
import org.eclipse.draw2d.ConnectionLayer
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart
import com.github.mdr.graphospasm.core.graph.xml._
import com.github.mdr.graphospasm.core.graph._

import com.github.mdr.graphospasm.grapheditor.part._
import com.github.mdr.graphospasm.grapheditor.model._

import org.eclipse.swt.graphics.GC
import org.eclipse.gef.ui.parts._
import org.eclipse.gef._
import org.eclipse.core.runtime._
import org.eclipse.ui.{ IEditorInput, IFileEditorInput }
import scala.xml._

class GraphEditor extends GraphicalEditorWithFlyoutPalette {

  private final val editDomain = new DefaultEditDomain(this)
  setEditDomain(editDomain)

  override def getPaletteRoot = new GraphEditorPalette

  private var diagram: GraphDiagram = _

  override def configureGraphicalViewer() {
    super.configureGraphicalViewer()
    val viewer = getGraphicalViewer

    viewer.setEditPartFactory(GraphEditPartFactory)

    viewer.setContents(diagram)
    editDomain.addViewer(viewer)
    configureConnectionRouting()
    useAntialiasingForConnections()
  }

  private def configureConnectionRouting() {
    val contentEditPart = getRootEditPart.getContents.asInstanceOf[GraphicalEditPart]
    val fanRouter = new FanRouter
    fanRouter.setSeparation(20)
    fanRouter.setNextRouter(new ShortestPathConnectionRouter(contentEditPart.getFigure))
    getConnectionLayer.setConnectionRouter(fanRouter)
  }

  private def useAntialiasingForConnections() {
    if ((getGraphicalViewer.getControl.getStyle & SWT.MIRRORED) == 0)
      getConnectionLayer.setAntialias(SWT.ON)
  }

  private final def getConnectionLayer = getRootEditPart.getLayer(LayerConstants.CONNECTION_LAYER).asInstanceOf[ConnectionLayer]

  override def getGraphicalViewer = super.getGraphicalViewer.asInstanceOf[ScrollingGraphicalViewer]

  private final def getRootEditPart = getGraphicalViewer.getRootEditPart.asInstanceOf[ScalableRootEditPart]

  override def setInput(input: IEditorInput) {
    super.setInput(input)
    val file = input.asInstanceOf[IFileEditorInput].getFile()
    val xml = XML.load(file.getContents)
    diagram = DummyDataGetter.createDiagram(xml)
    DummyDataGetter.autolayoutDiagram(diagram)

    setPartName(file.getName())
  }

  def doSave(monitor: IProgressMonitor) {
    //    val file = getEditorInput.asInstanceOf[IFileEditorInput].getFile
    //    file.setContents(new ByteArrayInputStream(modelAsText.getBytes), true, false, monitor)
    //    getCommandStack().markSaveLocation()
  }

}