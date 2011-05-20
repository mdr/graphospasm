package com.github.mdr.graphospasm.grapheditor

import com.github.mdr.graphospasm.grapheditor.part.NodeEditPart
import com.github.mdr.graphospasm.grapheditor.part.AttributeNameEditPart
import com.github.mdr.graphospasm.grapheditor.model.commands.AddAttributeCommand
import org.eclipse.gef.RequestConstants
import org.eclipse.gef.Request
import org.eclipse.gef.EditPart
import com.github.mdr.graphospasm.grapheditor.model.commands.CreateNodeCommand
import org.eclipse.gef.commands.Command
import org.eclipse.gef.requests.CreationFactory
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.gef.palette._
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.gef.palette._
import org.eclipse.gef.tools._

object GraphDiagramEditorPalette {

  val PREFIX = "com.github.mdr.graphospasm."

}

class GraphDiagramEditorPalette extends PaletteRoot {

  import GraphDiagramEditorPalette._

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
    val entry = new ToolEntry("Vertex", "Creates a new vertex", Plugin.newNode16Descriptor, Plugin.newNode24Descriptor, classOf[VertexCreationTool]) {
      setToolProperty(CreationTool.PROPERTY_CREATION_FACTORY, new NodeFactory)
    }
    entry.setId("palette.vertex")
    paletteGroup.add(entry)
  }
  {
    val entry = new ToolEntry("Attribute", "Creates a new attribute", Plugin.addAttribute16Descriptor, Plugin.addAttribute16Descriptor, classOf[AttributeCreationTool]) {
      setToolProperty(CreationTool.PROPERTY_CREATION_FACTORY, new AttributeFactory)
    }
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

class AttributeCreationTool extends CreationTool {
  override def executeCommand(command: Command) {
    super.executeCommand(command)
    val addAttributeCommand = command.asInstanceOf[AddAttributeCommand]
    val node = addAttributeCommand.node
    val editPartRegistry = getCurrentViewer.getEditPartRegistry

    val nodeEditPart = editPartRegistry.get(node).asInstanceOf[NodeEditPart]
    nodeEditPart.refreshVisuals()
    nodeEditPart.refreshChildren()
    nodeEditPart.getParent.getFigure.validate()
    nodeEditPart.getFigure.validate()

    val request = new Request(RequestConstants.REQ_DIRECT_EDIT)
    val attributeNameEditPart = editPartRegistry.get(addAttributeCommand.attributeName).asInstanceOf[AttributeNameEditPart]
    attributeNameEditPart.performRequest(request)
  }
}

class VertexCreationTool extends CreationTool {
  override def executeCommand(command: Command) {
    super.executeCommand(command)
    val node = command.asInstanceOf[CreateNodeCommand].node
    val editPartRegistry = getCurrentViewer.getEditPartRegistry

    val nodeEditPart = editPartRegistry.get(node).asInstanceOf[NodeEditPart]
    nodeEditPart.refreshVisuals()
    nodeEditPart.getParent.getFigure.validate()
    nodeEditPart.getFigure.validate()

    val request = new Request(RequestConstants.REQ_DIRECT_EDIT)
    nodeEditPart.performRequest(request)
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