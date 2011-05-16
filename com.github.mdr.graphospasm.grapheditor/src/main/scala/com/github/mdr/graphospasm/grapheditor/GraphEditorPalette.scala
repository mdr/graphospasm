package com.github.mdr.graphospasm.grapheditor

import org.eclipse.gef.requests.CreationFactory
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.gef.palette._
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.gef.palette._
import org.eclipse.gef.tools._

object GraphEditorPalette {

  val PREFIX = "com.github.mdr.graphospasm."

}

class GraphEditorPalette extends PaletteRoot {

  import GraphEditorPalette._

  private final val paletteGroup = new PaletteGroup("Graph Controls")

  add(paletteGroup)

  {
    val entry = new PanningSelectionToolEntry
    entry.setId(PREFIX + "palette.selection")
    paletteGroup.add(entry)
    setDefaultEntry(entry)
  }
  {
    val entry = new MarqueeToolEntry
    entry.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, MarqueeSelectionTool.BEHAVIOR_NODES_AND_CONNECTIONS)
    entry.setId(PREFIX + "palette.marquee")
    paletteGroup.add(entry)
  }
  {
    val separator = new PaletteSeparator
    separator.setId(PREFIX + "palette.separator")
    paletteGroup.add(separator)
  }
  {
    val entry = new CreationToolEntry("Vertex", "Creates a new vertex", new NodeFactory, Plugin.newNode16Descriptor,
      Plugin.newNode24Descriptor)
    entry.setId("palette.vertex")
    paletteGroup.add(entry)
  }
  {
    val entry = new CreationToolEntry("Attribute", "Creates a new attribute", new AttributeFactory, Plugin.addAttribute16Descriptor,
      Plugin.addAttribute16Descriptor)
    entry.setId("palette.attribute")
    paletteGroup.add(entry)
  }
  {
    val entry = new ConnectionCreationToolEntry("Edge", "Creates a new edge", new ConnectionInProgressFactory,
      Plugin.newConnection16Descriptor, Plugin.newConnection24Descriptor)
    entry.setId("palette.connection")
    paletteGroup.add(entry)
  }
}

class Attribute

class AttributeFactory extends CreationFactory {

  def getNewObject = new Attribute

  def getObjectType = classOf[Attribute]

}

class ConnectionInProgress

class ConnectionInProgressFactory extends CreationFactory {

  def getNewObject = new ConnectionInProgress

  def getObjectType = classOf[ConnectionInProgress]

}