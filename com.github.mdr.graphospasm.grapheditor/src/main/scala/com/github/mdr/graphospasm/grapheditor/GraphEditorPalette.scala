package com.github.mdr.graphospasm.grapheditor
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
  //  {
  //    val entry = new ConnectionCreationToolEntry("Connection", "Creates a new connection.", new ConnectionFactory,
  //      ImageDescriptor.createFromFile(classOf[Plugin], "icons/connection_s16.gif"),
  //      ImageDescriptor.createFromFile(classOf[Plugin], "icons/connection_s24.gif"))
  //    entry.setId("palette.connection")
  //    paletteGroup.add(entry)
  //  }
}
