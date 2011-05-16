package com.github.mdr.graphospasm.grapheditor.model

import org.eclipse.draw2d.FigureUtilities
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.swt.graphics.Font
import scala.math.{ max, min }

case class NodeContentsLayoutInfo(
  nameBounds: Rectangle,
  attributeNameBounds: Map[AttributeName, Rectangle],
  attributeValueBounds: Map[AttributeValue, Rectangle],
  height: Int)

object NodeContentsLayouter {

  private val ATTRIBUTE_NAME_VALUE_GAP_X = 17
  private val NAME_ATTRIBUTES_GAP_Y = 6
  private val ATTRIBUTE_NAME_INDENT_X = 4

  def layout(node: Node, contentArea: Rectangle, font: Font): NodeContentsLayoutInfo = {
    implicit val f = font

    val (nameHeight, nameBounds) = {
      val text = node.name.name.simpleName
      val textDimension = FigureUtilities.getTextExtents(text, font)
      val textX = max((contentArea.width - textDimension.width) / 2, 0)
      val nameHeight = textDimension.height + 2
      val textWidth = min(textDimension.width, contentArea.width)
      (nameHeight, new Rectangle(textX, 2, textWidth, textDimension.height + 2))
    }
    var currentY = nameHeight + NAME_ATTRIBUTES_GAP_Y

    var attributeNameBounds: Map[AttributeName, Rectangle] = Map()
    var attributeValueBounds: Map[AttributeValue, Rectangle] = Map()

    val attributeMap = node.getAttributes.toMap
    if (attributeMap.nonEmpty) {
      val sortedAttributeNames = attributeMap.toList.map(_._1).sortBy(p ⇒ p.name.simpleName)
      val widestAttributeName = sortedAttributeNames.map(widthRequired).max
      val widestAttributeValue = sortedAttributeNames.map(widthRequired).max

      val attributeValueX = widestAttributeName + ATTRIBUTE_NAME_VALUE_GAP_X
      for ((attributeName, attributeValue) ← attributeMap.toList.sortBy(_._1.name.simpleName)) {
        val attributeNameDimension = {
          val dimension = dimensionRequired(attributeName)
          val bounds = new Rectangle(ATTRIBUTE_NAME_INDENT_X, currentY, dimension.width, dimension.height)
          attributeNameBounds += (attributeName -> bounds)
          dimension
        }
        {
          val dimension = dimensionRequired(attributeValue)
          val width = min(contentArea.width - attributeValueX, dimension.width)
          val bounds = new Rectangle(attributeValueX, currentY, width, dimension.height)
          attributeValueBounds += (attributeValue -> bounds)
        }
        currentY += attributeNameDimension.height
      }
    }
    NodeContentsLayoutInfo(nameBounds, attributeNameBounds, attributeValueBounds, height = currentY)
  }

  private def dimensionRequired(attributeName: AttributeName)(implicit font: Font) =
    FigureUtilities.getTextExtents(attributeName.name.simpleName, font)

  private def widthRequired(attributeName: AttributeName)(implicit font: Font) = dimensionRequired(attributeName).width

  private def dimensionRequired(attributeValue: AttributeValue)(implicit font: Font) =
    FigureUtilities.getTextExtents(attributeValue.presentationString, font)

  private def widthRequired(attributeValue: AttributeValue)(implicit font: Font) = dimensionRequired(attributeValue).width

}