package com.github.mdr.graphospasm.grapheditor.actions

import com.github.mdr.graphospasm.grapheditor.Plugin
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.ui.actions.RetargetAction

class AddEdgeLabelRetargetAction extends RetargetAction(AddEdgeLabelAction.actionId, "Add label to edge") {

  setImageDescriptor(Plugin.addEdgeLabel16)

}
