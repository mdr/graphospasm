package com.github.mdr.graphospasm.grapheditor

import java.net.URL
import org.eclipse.swt.graphics._
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.eclipse.jface.resource.ImageDescriptor

class Plugin extends AbstractUIPlugin {

  Plugin.plugin = this

}

object Plugin {

  def instance = plugin

  private var plugin: Plugin = _

  private def imageDescriptor(path: String) = {
    val url = new URL(instance.getDescriptor.getInstallURL, path)
    ImageDescriptor.createFromURL(url)
  }

  lazy val newNode16Descriptor = imageDescriptor("icons/newNode-16.png")
  lazy val newNode24Descriptor = imageDescriptor("icons/newNode-16.png")

  lazy val newConnection16Descriptor = imageDescriptor("icons/connection_s16.gif")
  lazy val newConnection24Descriptor = imageDescriptor("icons/connection_s24.gif")

}