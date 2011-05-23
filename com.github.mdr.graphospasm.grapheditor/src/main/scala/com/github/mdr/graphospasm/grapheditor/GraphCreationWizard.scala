package com.github.mdr.graphospasm.grapheditor

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.ObjectOutputStream
import org.eclipse.swt.widgets.Composite
import org.eclipse.core.resources.IFile
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.wizard.Wizard
import org.eclipse.ui.INewWizard
import org.eclipse.ui.IWorkbench
import org.eclipse.ui.IWorkbenchPage
import org.eclipse.ui.PartInitException
import org.eclipse.ui.dialogs.WizardNewFileCreationPage
import org.eclipse.ui.ide.IDE

object GraphCreationWizard {

  private var fileCount: Int = 1
  private val DEFAULT_EXTENSION = ".graph"

  val ID = "com.github.mdr.graphospasm.grapheditor.GraphCreationWizard"

}

class GraphCreationWizard extends Wizard with INewWizard {

  import GraphCreationWizard._

  private var page1: CreationPage = _

  override def addPages() { addPage(page1) }

  def init(workbench: IWorkbench, selection: IStructuredSelection) {
    page1 = new CreationPage(workbench, selection)
  }

  def performFinish() = page1.finish()

  class CreationPage(workbench: IWorkbench, selection: IStructuredSelection) extends WizardNewFileCreationPage("graphCreationPage", selection) {
    setTitle("Create a new " + DEFAULT_EXTENSION + " file")
    setDescription("Create a new " + DEFAULT_EXTENSION + " file")

    override def createControl(parent: Composite) {
      super.createControl(parent)
      setFileName("graph" + fileCount + DEFAULT_EXTENSION)
      setPageComplete(validatePage)
    }

    def finish(): Boolean = {
      val newFile = createNewFile()
      fileCount += 1

      val page = workbench.getActiveWorkbenchWindow.getActivePage
      if (newFile != null && page != null)
        IDE.openEditor(page, newFile, true)
      true
    }

    override def getInitialContents(): InputStream = new ByteArrayInputStream("<root/>".getBytes)

    private def validateFilename(): Boolean =
      if (getFileName != null && getFileName.endsWith(DEFAULT_EXTENSION))
        true
      else {
        setErrorMessage("The file name must end with " + DEFAULT_EXTENSION)
        false
      }

    override def validatePage = super.validatePage && validateFilename

  }
}
