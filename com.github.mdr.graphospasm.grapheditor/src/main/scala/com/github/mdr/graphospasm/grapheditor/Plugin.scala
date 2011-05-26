package com.github.mdr.graphospasm.grapheditor

import java.net.URL
import org.eclipse.swt.graphics._
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.eclipse.jface.resource.ImageDescriptor

class Plugin extends AbstractUIPlugin {

  Plugin.plugin = this

}

object Plugin {

  var suspendUpdates = false

  def instance = plugin

  private var plugin: Plugin = _

  private def imageDescriptor(path: String) =
    ImageDescriptor.createFromURL(instance.getBundle.getEntry(path))

  lazy val newNode16Descriptor = imageDescriptor("icons/newNode-16.png")
  lazy val newNode24Descriptor = imageDescriptor("icons/newNode-24.png")

  lazy val addAttribute16Descriptor = imageDescriptor("icons/addAttribute-16.png")

  lazy val addEdge16Descriptor = imageDescriptor("icons/addEdge-16.png")
  lazy val addEdgeWithLabel16Descriptor = imageDescriptor("icons/addEdgeWithLabel-16.png")

  lazy val relayout16 = imageDescriptor("icons/relayout-16.png")
  lazy val addEdgeLabel16 = imageDescriptor("icons/addEdgeLabel-16.png")

  val backgroundBlue = new Color(null, 200, 200, 240)

}