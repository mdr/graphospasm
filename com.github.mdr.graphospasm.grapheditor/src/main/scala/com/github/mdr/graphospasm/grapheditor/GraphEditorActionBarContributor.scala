package com.github.mdr.graphospasm.grapheditor

import com.github.mdr.graphospasm.grapheditor.actions.RelayoutAction
import com.github.mdr.graphospasm.grapheditor.actions.RelayoutRetargetAction
import org.eclipse.ui.IWorkbenchActionConstants
import org.eclipse.jface.action._
import org.eclipse.ui.actions.ActionFactory
import org.eclipse.jface.action.IToolBarManager
import org.eclipse.gef.ui.actions._
import org.eclipse.jface.action.IMenuManager
import org.eclipse.ui.actions.RetargetAction
import org.eclipse.gef.internal.GEFMessages
import org.eclipse.draw2d.PositionConstants

class GraphEditorActionBarContributor extends ActionBarContributor {

  override def buildActions() {

    //    addRetargetAction(new CopyRetargetAction)
    //    addRetargetAction(new PasteRetargetAction)
    //    // addRetargetAction(new CutRetargetAction)
    //
    addRetargetAction(new DeleteRetargetAction)
    addRetargetAction(new UndoRetargetAction)
    addRetargetAction(new RedoRetargetAction)
    addRetargetAction(new ZoomInRetargetAction)
    addRetargetAction(new ZoomOutRetargetAction)

    addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT))
    addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER))
    addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT))
    addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP))
    addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE))
    addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM))

    addRetargetAction(new MatchWidthRetargetAction)
    addRetargetAction(new MatchHeightRetargetAction)

    addRetargetAction(new RelayoutRetargetAction)
    //
    //    //    addRetargetAction(new RetargetAction(
    //    //      GEFActionConstants.TOGGLE_RULER_VISIBILITY,
    //    //      GEFMessages.ToggleRulerVisibility_Label, IAction.AS_CHECK_BOX))
    //
    addRetargetAction(new RetargetAction(
      GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY,
      GEFMessages.ToggleSnapToGeometry_Label, IAction.AS_CHECK_BOX))
    //
    //    addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY,
    //      GEFMessages.ToggleGrid_Label, IAction.AS_CHECK_BOX))

  }

  def declareGlobalActionKeys {
    //    addGlobalActionKey(ActionFactory.PRINT.getId)
    addGlobalActionKey(ActionFactory.SELECT_ALL.getId)
    //    addGlobalActionKey(ActionFactory.CUT.getId)
    //    addGlobalActionKey(ActionFactory.COPY.getId)
    //    addGlobalActionKey(ActionFactory.PASTE.getId)
    addGlobalActionKey(ActionFactory.DELETE.getId)
  }

  override def contributeToToolBar(tbm: IToolBarManager) {
    import tbm.add
    add(getAction(ActionFactory.DELETE.getId))
    add(getAction(ActionFactory.UNDO.getId))
    add(getAction(ActionFactory.REDO.getId))

    add(new Separator())

    add(getAction(GEFActionConstants.ALIGN_LEFT))
    add(getAction(GEFActionConstants.ALIGN_CENTER))
    add(getAction(GEFActionConstants.ALIGN_RIGHT))

    add(new Separator)

    add(getAction(GEFActionConstants.ALIGN_TOP))
    add(getAction(GEFActionConstants.ALIGN_MIDDLE))
    add(getAction(GEFActionConstants.ALIGN_BOTTOM))

    add(new Separator)

    add(getAction(GEFActionConstants.MATCH_WIDTH))
    add(getAction(GEFActionConstants.MATCH_HEIGHT))

    add(new Separator)
    //
    add(getAction(RelayoutAction.actionId))
    //
    //    add(new Separator)
    //
    add(new ZoomComboContributionItem(getPage, Array[String]()))
  }

  override def contributeToMenu(menubar: IMenuManager) {
    super.contributeToMenu(menubar)
    val viewMenu = new MenuManager("View")
    viewMenu.add(getAction(GEFActionConstants.ZOOM_IN))
    viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT))
    viewMenu.add(new Separator)
    //    //    viewMenu.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY))
    //    viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY))
    viewMenu.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY))
    viewMenu.add(new Separator)
    viewMenu.add(getAction(GEFActionConstants.MATCH_WIDTH))
    viewMenu.add(getAction(GEFActionConstants.MATCH_HEIGHT))
    menubar.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu)
  }

}
