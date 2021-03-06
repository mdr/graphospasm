package com.github.mdr.graphospasm.grapheditor.model
import com.github.mdr.graphospasm.core.graph._
import com.github.mdr.graphospasm.grapheditor.utils.Utils
import org.eclipse.draw2d.FigureUtilities
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.swt.graphics.Font
import scala.math.{ max, min }
import org.eclipse.draw2d.geometry.Dimension
import com.github.mdr.graphospasm.grapheditor.namespace.NamespacePrefixManager

case class NodeContentsLayoutInfo(
  nameBounds: Rectangle,
  attributeNameBounds: Map[AttributeName, Rectangle],
  attributeValueBounds: Map[AttributeValue, Rectangle],
  minimumRequiredWidth: Int,
  minimumRequiredHeight: Int,
  attributeNameColumnWidth: Int,
  attributeValueColumnWidth: Int)

object NodeContentsLayouter {

  private final val ATTRIBUTE_NAME_VALUE_GAP_X = 13
  private final val NAME_ATTRIBUTES_GAP_Y = 4
  private final val NAME_INDENT_Y = 2
  private final val ATTRIBUTE_NAME_INDENT_X = 4
  private final val SHADOW_SIZE = 6
  private final val NAME_HEIGHT_ADJUST = 4
  private final val RIGHT_PADDING = 4
  private final val BOTTOM_PADDING = 4
  private final val MINIMUM_ATTRIBUTE_VALUE_WIDTH_REQUIREMENT = new Dimension(10, 0)

  def preferredWidth(node: Node, font: Font, namespacePrefixManager: NamespacePrefixManager = NoNamespacePrefixManager) = {
    implicit val f = font
    implicit val npm = namespacePrefixManager
    val nameDimension = Utils.getTextExtents(node.name.simpleName)
    val nameWidth = nameDimension.width
    var preferredWidth = nameWidth + 32
    val attributeMap = node.getAttributes.toMap
    if (attributeMap.nonEmpty) {
      val sortedAttributeNames = attributeMap.toList.map(_._1)
      val widestAttributeName = sortedAttributeNames.map(widthRequired).max
      val widestAttributeValue = sortedAttributeNames.map(n ⇒ widthRequired(attributeMap(n))).max
      preferredWidth = max(preferredWidth,
        ATTRIBUTE_NAME_INDENT_X + widestAttributeName + ATTRIBUTE_NAME_VALUE_GAP_X + widestAttributeValue) + SHADOW_SIZE + RIGHT_PADDING
    }
    preferredWidth
  }

  object NoNamespacePrefixManager extends NamespacePrefixManager {

    def getPrefix(name: Name): String = ""

    def getDisplayName(name: Name): String = name.simpleName

    def registerPrefix(namespace: String, prefix: String) {}

    def setShowNamespaces(showNamespaces: Boolean) {}

  }

  def layout(node: Node, font: Font, namespacePrefixManager: NamespacePrefixManager = NoNamespacePrefixManager): NodeContentsLayoutInfo = {
    val contentArea = node.bounds.getResized(-SHADOW_SIZE, -SHADOW_SIZE)

    implicit val f = font
    implicit val npm = namespacePrefixManager
    val nameDimension = Utils.getTextExtents(namespacePrefixManager.getDisplayName(node.name))
    val nameX = max((contentArea.width - nameDimension.width) / 2, 0)
    val nameHeight = nameDimension.height + NAME_HEIGHT_ADJUST
    val nameWidth = min(nameDimension.width, contentArea.width)
    val nameBounds = new Rectangle(nameX, NAME_INDENT_Y, nameWidth, nameHeight)

    var minimumRequiredWidth = nameWidth
    var currentY = NAME_INDENT_Y + nameHeight

    var attributeNameBounds: Map[AttributeName, Rectangle] = Map()
    var attributeValueBounds: Map[AttributeValue, Rectangle] = Map()

    val attributeMap = node.getAttributes.toMap
    var attributeValueColumnWidth = 0
    var attributeNameColumnWidth = 0
    if (attributeMap.nonEmpty) {
      currentY += NAME_ATTRIBUTES_GAP_Y
      val sortedAttributeNames = attributeMap.toList.map(_._1).sortBy(p ⇒ p.name.simpleName)
      val widestAttributeName = sortedAttributeNames.map(widthRequired).max
      val widestAttributeValue = sortedAttributeNames.map(n ⇒ widthRequired(attributeMap(n))).max

      val attributeValueX = ATTRIBUTE_NAME_INDENT_X + widestAttributeName + ATTRIBUTE_NAME_VALUE_GAP_X
      attributeNameColumnWidth = widestAttributeName
      attributeValueColumnWidth = contentArea.width - attributeValueX - SHADOW_SIZE

      for ((attributeName, attributeValue) ← attributeMap.toList.sortBy(_._1.name.simpleName)) {
        val attributeNameDimension = {
          val dimension = dimensionRequired(attributeName)
          val width = min(contentArea.width - ATTRIBUTE_NAME_INDENT_X, dimension.width)
          val bounds = new Rectangle(ATTRIBUTE_NAME_INDENT_X, currentY, width, dimension.height)
          attributeNameBounds += (attributeName -> bounds)
          dimension
        }
        val attributeValueDimension = {
          val dimension = dimensionRequired(attributeValue)
          val width = min(contentArea.width - attributeValueX, dimension.width)
          val bounds = new Rectangle(attributeValueX, currentY, width, dimension.height)
          attributeValueBounds += (attributeValue -> bounds)
          dimension
        }
        currentY += max(attributeNameDimension.height, attributeValueDimension.height)
      }
      minimumRequiredWidth = max(minimumRequiredWidth,
        ATTRIBUTE_NAME_INDENT_X + widestAttributeName + ATTRIBUTE_NAME_VALUE_GAP_X + widestAttributeValue)
    }
    minimumRequiredWidth += SHADOW_SIZE + RIGHT_PADDING
    val minimumRequiredHeight = currentY + SHADOW_SIZE + BOTTOM_PADDING
    NodeContentsLayoutInfo(nameBounds, attributeNameBounds, attributeValueBounds, minimumRequiredWidth, minimumRequiredHeight, attributeNameColumnWidth, attributeValueColumnWidth)
  }

  private def dimensionRequired(attributeName: AttributeName)(implicit font: Font, namespacePrefixManager: NamespacePrefixManager) =
    Utils.getTextExtents(namespacePrefixManager.getDisplayName(attributeName.name))

  private def widthRequired(attributeName: AttributeName)(implicit font: Font, namespacePrefixManager: NamespacePrefixManager) = dimensionRequired(attributeName).width

  private def dimensionRequired(attributeValue: AttributeValue)(implicit font: Font, namespacePrefixManager: NamespacePrefixManager) = {
    var basic = Utils.getTextExtents(attributeValue.presentationString)
    if (attributeValue.value.isInstanceOf[java.lang.Integer])
      basic = basic.getExpanded(16, 0)
    basic.getUnioned(MINIMUM_ATTRIBUTE_VALUE_WIDTH_REQUIREMENT)
  }

  private def widthRequired(attributeValue: AttributeValue)(implicit font: Font, namespacePrefixManager: NamespacePrefixManager) = dimensionRequired(attributeValue).width

}