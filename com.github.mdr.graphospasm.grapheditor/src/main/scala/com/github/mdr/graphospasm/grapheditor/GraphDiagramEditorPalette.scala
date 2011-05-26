package com.github.mdr.graphospasm.grapheditor
import com.github.mdr.graphospasm.core.graph._
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
import com.github.mdr.graphospasm.grapheditor.model.commands.SetEdgeLabelCommand
import com.github.mdr.graphospasm.grapheditor.part.ConnectionEditPart
import com.github.mdr.graphospasm.grapheditor.model.commands.CreateConnectionCommand

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
    val entry = new ToolEntry("Edge", "Creates a new edge", Plugin.addEdge16Descriptor,
      Plugin.addEdge16Descriptor, classOf[EdgeCreationTool]) {
      setToolClass(classOf[EdgeCreationTool])
      setToolProperty(CreationTool.PROPERTY_CREATION_FACTORY, new ConnectionInProgressFactory(withLabel = false))
    }
    entry.setId("palette.edgeWithLabel")
    paletteGroup.add(entry)
  }
  {
    val entry = new ToolEntry("Edge with label", "Creates a new edge with a label", Plugin.addEdgeWithLabel16Descriptor,
      Plugin.addEdgeWithLabel16Descriptor, classOf[EdgeCreationTool]) {
      setToolProperty(CreationTool.PROPERTY_CREATION_FACTORY, new ConnectionInProgressFactory(withLabel = true))
    }
    entry.setId("palette.edgeWithLabel")
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
    val entry = new ToolEntry("Add edge label", "Creates a new edge label", Plugin.addEdgeLabel16, Plugin.addEdgeLabel16, classOf[EdgeLabelCreationTool]) {
      setToolProperty(CreationTool.PROPERTY_CREATION_FACTORY, new EdgeLabelFactory)
    }
    entry.setId("palette.attribute")
    paletteGroup.add(entry)
  }

}

class EdgeLabelCreationTool extends CreationTool {
  override def executeCommand(command: Command) {
    super.executeCommand(command)
    val setEdgeLabelCommand = command.asInstanceOf[SetEdgeLabelCommand]

    val connection = setEdgeLabelCommand.connection
    val editPartRegistry = getCurrentViewer.getEditPartRegistry
    val connectionEditPart = editPartRegistry.get(connection).asInstanceOf[ConnectionEditPart]

    val request = new Request(RequestConstants.REQ_DIRECT_EDIT)
    connectionEditPart.performRequest(request)
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

class EdgeCreationTool extends ConnectionCreationTool {
  override def executeCommand(command: Command) {
    super.executeCommand(command)
    val createConnectionCommand = command.asInstanceOf[CreateConnectionCommand]
    val connection = createConnectionCommand.connection

    val editPartRegistry = getCurrentViewer.getEditPartRegistry
    val connectionEditPart = editPartRegistry.get(connection).asInstanceOf[ConnectionEditPart]

    val request = new Request(RequestConstants.REQ_DIRECT_EDIT)
    connectionEditPart.performRequest(request)
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

class ConnectionInProgress(val labelOpt: Option[Name])

class ConnectionInProgressFactory(withLabel: Boolean) extends CreationFactory {

  def getNewObject = new ConnectionInProgress(if (withLabel) Some(Name("label")) else None)

  def getObjectType = classOf[ConnectionInProgress]

}

class EdgeLabel

class EdgeLabelFactory extends CreationFactory {

  def getNewObject = new EdgeLabel

  def getObjectType = classOf[EdgeLabel]

}