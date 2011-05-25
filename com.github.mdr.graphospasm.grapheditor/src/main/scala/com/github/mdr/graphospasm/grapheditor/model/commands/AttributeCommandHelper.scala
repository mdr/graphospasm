package com.github.mdr.graphospasm.grapheditor.model.commands

import com.github.mdr.graphospasm.grapheditor.utils.Utils
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.model._
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.gef.commands.Command

object AttributeCommandHelper {

  def expandToFitAttributesIfNecessary(node: Node): Option[Dimension] =
    Utils.withFont { font ⇒
      val nodeContentsLayoutInfo = NodeContentsLayouter.layout(node, font)
      if (node.height < nodeContentsLayoutInfo.minimumRequiredHeight) {
        val previousDimension = node.size
        val newSize = node.size.getCopy
        newSize.height = nodeContentsLayoutInfo.minimumRequiredHeight
        node.size = newSize
        Some(previousDimension)
      } else
        None
    }

}