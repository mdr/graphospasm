package com.github.mdr.graphospasm.grapheditor

import org.eclipse.ui.views.contentoutline.IContentOutlinePage
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.custom.SashForm
import org.eclipse.swt.events.DisposeEvent
import org.eclipse.swt.events.DisposeListener
import org.eclipse.draw2d.Viewport
import org.eclipse.draw2d.parts.ScrollableThumbnail
import org.eclipse.draw2d.LightweightSystem
import org.eclipse.swt.widgets.Canvas
import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.FreeformLayer
import org.eclipse.draw2d.ScalableFreeformLayeredPane
import java.util.EventObject
import org.eclipse.ui.IEditorPart
import org.eclipse.draw2d.PositionConstants
import org.eclipse.gef.ui.actions._
import org.eclipse.jface.commands.ActionHandler
import org.eclipse.ui.handlers.IHandlerService
import org.eclipse.gef.editparts.ZoomManager
import org.eclipse.gef.ui.actions.ZoomOutAction
import org.eclipse.gef.ui.actions.ZoomInAction
import org.eclipse.gef.commands.CommandStack
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
import org.eclipse.ui.{ IEditorInput, IFileEditorInput, IWorkbenchPart }
import scala.xml._
import scala.collection.JavaConversions._
import org.eclipse.jface.viewers.ISelection

class GraphEditor extends GraphicalEditorWithFlyoutPalette {

  setEditDomain(new DefaultEditDomain(this))

  override def getPaletteRoot = new GraphEditorPalette

  private var diagram: GraphDiagram = _

  override def configureGraphicalViewer() {
    super.configureGraphicalViewer()
    val viewer = getGraphicalViewer

    val rootEditPart = new TweakedScalableFreeformRootEditPart
    viewer.setRootEditPart(rootEditPart)

    viewer.setEditPartFactory(GraphEditPartFactory)
    viewer.setContents(diagram)

    getEditDomain.addViewer(viewer)
    configureConnectionRouting()
    useAntialiasingForConnections()
    configureZoom()
    configureSnapToGeometry(viewer)
    addKeyBindingForDirectEdit()
    setUpContextMenu()
  }

  private def setUpContextMenu() {
    val viewer = getGraphicalViewer
    val contextMenuProvider = new GraphEditorContextMenuProvider(viewer, getActionRegistry)
    viewer.setContextMenu(contextMenuProvider)
    getSite.registerContextMenu(contextMenuProvider, viewer)
  }

  private def addKeyBindingForDirectEdit() {
    val keyHandler = new GraphicalViewerKeyHandler(getGraphicalViewer)
    getGraphicalViewer.setKeyHandler(keyHandler)
    val directEditAction = getActionRegistry.getAction(GEFActionConstants.DIRECT_EDIT)
    keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), directEditAction)
  }

  private def configureZoom() {
    val zoomManager = getRootEditPart.getZoomManager
    zoomManager.setZoomLevels(0.2 to 1.0 by 0.05 toArray)
    zoomManager.setZoomLevelContributions(List(ZoomManager.FIT_ALL, ZoomManager.FIT_WIDTH, ZoomManager.FIT_HEIGHT))
    val zoomIn = new ZoomInAction(zoomManager)
    val zoomOut = new ZoomOutAction(zoomManager)
    getActionRegistry.registerAction(zoomIn)
    getActionRegistry.registerAction(zoomOut)
    getHandlerService.activateHandler(zoomIn.getActionDefinitionId, new ActionHandler(zoomIn))
    getHandlerService.activateHandler(zoomOut.getActionDefinitionId, new ActionHandler(zoomOut))
    getGraphicalViewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.None), MouseWheelZoomHandler.SINGLETON)
  }

  private def configureSnapToGeometry(viewer: org.eclipse.gef.ui.parts.ScrollingGraphicalViewer) {
    val snapAction = new ToggleSnapToGeometryAction(getGraphicalViewer)
    getActionRegistry.registerAction(snapAction)
    viewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, true)
  }

  private final def getHandlerService = getSite.getService(classOf[IHandlerService]).asInstanceOf[IHandlerService]

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

  override def commandStackChanged(event: EventObject) {
    firePropertyChange(IEditorPart.PROP_DIRTY)
    super.commandStackChanged(event)
  }

  private final def getConnectionLayer = getRootEditPart.getLayer(LayerConstants.CONNECTION_LAYER).asInstanceOf[ConnectionLayer]

  override def getGraphicalViewer = super.getGraphicalViewer.asInstanceOf[ScrollingGraphicalViewer]

  private final def getRootEditPart = getGraphicalViewer.getRootEditPart.asInstanceOf[ScalableFreeformRootEditPart]

  override def setInput(input: IEditorInput) {
    super.setInput(input)
    val file = input.asInstanceOf[IFileEditorInput].getFile()
    val xml = XML.load(file.getContents)
    val importSpec = XmlImportSpec(blackList = true, directives = List())
    val graph = new XmlImporter(importSpec).makeGraph(xml)
    diagram = GraphDiagram.create(graph)
    DummyDataGetter.autolayoutDiagram(diagram)

    setPartName(file.getName)
  }

  def doSave(monitor: IProgressMonitor) {
    //    val file = getEditorInput.asInstanceOf[IFileEditorInput].getFile
    //    file.setContents(new ByteArrayInputStream(modelAsText.getBytes), true, false, monitor)
    //    getCommandStack().markSaveLocation()
  }

  override def getAdapter(type_ : Class[_]) =
    if (type_ == classOf[CommandStack])
      getEditDomain.getCommandStack
    else if (type_ == classOf[ZoomManager])
      getGraphicalViewer.getProperty(classOf[ZoomManager].toString)
    else if (type_ == classOf[IContentOutlinePage])
      new GraphEditorOutlinePage()
    else
      super.getAdapter(type_)

  override def selectionChanged(part: IWorkbenchPart, selection: ISelection) {
    updateActions(getSelectionActions())
  }

  override def createActions() {
    super.createActions()
    val registry = getActionRegistry()

    def createSelectionAction(action: SelectionAction) = {
      registry.registerAction(action)
      getSelectionActions.asInstanceOf[java.util.List[String]].add(action.getId)
    }
    val workbenchPart = this.asInstanceOf[IWorkbenchPart]

    registry.registerAction(new SelectAllAction(this))

    //    createSelectionAction(new CopyAction(this))
    //    createSelectionAction(new PasteAction(this))
    //    createSelectionAction(new CutAction(this))
    //    createSelectionAction(new DirectEditAction(this))
    //    createSelectionAction(new RelayoutAction(this))
    createSelectionAction(new MatchWidthAction(this))
    createSelectionAction(new MatchHeightAction(this))
    createSelectionAction(new AlignmentAction(workbenchPart, PositionConstants.LEFT))
    createSelectionAction(new AlignmentAction(workbenchPart, PositionConstants.RIGHT))
    createSelectionAction(new AlignmentAction(workbenchPart, PositionConstants.TOP))
    createSelectionAction(new AlignmentAction(workbenchPart, PositionConstants.BOTTOM))
    createSelectionAction(new AlignmentAction(workbenchPart, PositionConstants.CENTER))
    createSelectionAction(new AlignmentAction(workbenchPart, PositionConstants.MIDDLE))

  }

  class GraphEditorOutlinePage extends ContentOutlinePage(new TreeViewer) with IAdaptable {
    private var sash: SashForm = _
    override def createControl(parent: Composite) {
      sash = new SashForm(parent, SWT.VERTICAL)
      val canvas = new Canvas(sash, SWT.BORDER)
      val lws = new LightweightSystem(canvas)
      val thumbnail = new ScrollableThumbnail(getRootEditPart.getFigure.asInstanceOf[Viewport])
      thumbnail.setSource(getRootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS))
      lws.setContents(thumbnail)
      val disposeListener = new DisposeListener() {
        def widgetDisposed(e: DisposeEvent) {
          if (thumbnail != null) {
            thumbnail.deactivate()
            // thumbnail = null
          }
        }
      }
      getGraphicalViewer.getControl.addDisposeListener(disposeListener)
    }

    override def getControl = sash // getViewer.getControl

    override def dispose {
      getSelectionSynchronizer.removeViewer(getViewer)
      super.dispose
    }

    def getAdapter(type_ : Class[_]) =
      if (type_ == classOf[ZoomManager])
        getGraphicalViewer.getProperty(classOf[ZoomManager].toString)
      else
        null
  }

}