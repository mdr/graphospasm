package com.github.mdr.graphospasm.grapheditor

import org.eclipse.swt.graphics._
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.eclipse.jface.resource.ImageDescriptor

class Plugin extends AbstractUIPlugin {

  Plugin.plugin = this

}

object Plugin {

  def instance = plugin

  private var plugin: Plugin = _

  private def makeImage(path: String): Image = ImageDescriptor.createFromURL(classOf[Plugin].getResource(path)).createImage

}