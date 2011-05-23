package com.github.mdr.graphospasm.grapheditor
import org.eclipse.ui._
import org.eclipse.ui.IPageLayout._
import org.eclipse.gef.ui.views.palette._

class GraphospasmPerspectiveFactory extends IPerspectiveFactory {

  def createInitialLayout(layout: IPageLayout) {
    val editorArea = layout.getEditorArea
    layout.addView(ID_PROJECT_EXPLORER, LEFT, 0.25f, editorArea)
    layout.addView(ID_PROP_SHEET, BOTTOM, 0.75f, editorArea)
    layout.addView(PaletteView.ID, BOTTOM, 0.50f, ID_PROJECT_EXPLORER)
    layout.addView(ID_OUTLINE, BOTTOM, 0.60f, PaletteView.ID)
    layout.addNewWizardShortcut(GraphCreationWizard.ID)
    layout.addShowViewShortcut(PaletteView.ID)
    layout.addShowViewShortcut(ID_PROP_SHEET)
    layout.addShowViewShortcut(ID_OUTLINE)
  }

}