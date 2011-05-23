package com.github.mdr.graphospasm.grapheditor.actions

import com.github.mdr.graphospasm.grapheditor.Plugin
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.ui.actions.RetargetAction

class RelayoutRetargetAction extends RetargetAction(RelayoutAction.actionId, "Relayout the graph") {

  setImageDescriptor(Plugin.relayout16)

}
