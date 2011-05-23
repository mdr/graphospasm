package com.github.mdr.graphospasm.grapheditor

import com.github.mdr.graphospasm.grapheditor.actions.RelayoutAction
import org.eclipse.jface.action.IAction
import org.eclipse.jface.action.IMenuManager
import org.eclipse.ui.actions.ActionFactory
import org.eclipse.gef.ContextMenuProvider
import org.eclipse.gef.EditPartViewer
import org.eclipse.gef.ui.actions.ActionRegistry
import org.eclipse.gef.ui.actions.GEFActionConstants
import org.eclipse.jface.action.MenuManager
import org.eclipse.jface.action.Separator

class GraphDiagramEditorContextMenuProvider(viewer: EditPartViewer, actionRegistry: ActionRegistry) extends ContextMenuProvider(viewer) {

  def buildContextMenu(menu: IMenuManager) {
    GEFActionConstants.addStandardActionGroups(menu)

    def getAction(actionId: String) = actionRegistry.getAction(actionId)
    menu.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.UNDO.getId))
    menu.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.REDO.getId))

    val cutAction = actionRegistry.getAction(ActionFactory.CUT.getId)
    if (cutAction.isEnabled)
      menu.appendToGroup(GEFActionConstants.GROUP_COPY, cutAction)

    val copyAction = actionRegistry.getAction(ActionFactory.COPY.getId)
    if (copyAction.isEnabled)
      menu.appendToGroup(GEFActionConstants.GROUP_COPY, copyAction)

    val pasteAction = actionRegistry.getAction(ActionFactory.PASTE.getId)
    if (pasteAction.isEnabled)
      menu.appendToGroup(GEFActionConstants.GROUP_COPY, pasteAction)

    menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.SELECT_ALL.getId))
    menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.DELETE.getId))

    val editAction = getAction(GEFActionConstants.DIRECT_EDIT)
    if (editAction.isEnabled)
      menu.appendToGroup(GEFActionConstants.GROUP_EDIT, editAction)

    val relayoutAction = getAction(RelayoutAction.actionId)
    if (relayoutAction.isEnabled)
      menu.appendToGroup(GEFActionConstants.GROUP_REST, relayoutAction)

    val submenu = new MenuManager("Align")

    var action = actionRegistry.getAction(GEFActionConstants.ALIGN_LEFT)
    if (action.isEnabled)
      submenu.add(action)

    action = actionRegistry.getAction(GEFActionConstants.ALIGN_CENTER)
    if (action.isEnabled)
      submenu.add(action)

    action = actionRegistry.getAction(GEFActionConstants.ALIGN_RIGHT)
    if (action.isEnabled)
      submenu.add(action)

    submenu.add(new Separator)

    action = actionRegistry.getAction(GEFActionConstants.ALIGN_TOP)
    if (action.isEnabled)
      submenu.add(action)

    action = actionRegistry.getAction(GEFActionConstants.ALIGN_MIDDLE)
    if (action.isEnabled)
      submenu.add(action)

    action = actionRegistry.getAction(GEFActionConstants.ALIGN_BOTTOM)
    if (action.isEnabled)
      submenu.add(action)

    if (!submenu.isEmpty)
      menu.appendToGroup(GEFActionConstants.GROUP_REST, submenu)

    val saveAction = actionRegistry.getAction(ActionFactory.SAVE.getId)
    menu.appendToGroup(GEFActionConstants.GROUP_SAVE, saveAction)

  }
}

