package com.github.mdr.graphospasm.grapheditor

import com.github.mdr.graphospasm.grapheditor.part._
import org.eclipse.gef.GraphicalEditPart
import scala.collection.JavaConversions._
import org.eclipse.draw2d.PositionConstants._
import scala.math.abs

object NodeContentsKeyNavigator {

  def navigate(currentPart: GraphicalEditPart, direction: Int, siblingParts: java.util.List[GraphicalEditPart]): GraphicalEditPart = {
    actualNavigate(currentPart, direction, siblingParts.toList).getOrElse(null)
  }

  private def actualNavigate(currentPart: GraphicalEditPart, direction: Int, siblingParts: List[GraphicalEditPart]): Option[GraphicalEditPart] =
    currentPart match {
      case part: NodeNameEditPart       ⇒ navigate(part, direction, siblingParts)
      case part: AttributeNameEditPart  ⇒ navigate(part, direction, siblingParts)
      case part: AttributeValueEditPart ⇒ navigate(part, direction, siblingParts)
      case _                            ⇒ None
    }

  private def navigate(currentPart: NodeNameEditPart, direction: Int, siblingParts: List[GraphicalEditPart]): Option[GraphicalEditPart] =
    direction match {
      case SOUTH ⇒
        siblingParts.collect { case p: AttributeNameEditPart ⇒ p }.sortBy(_.getFigure.getBounds.y).headOption
      case LEFT | RIGHT | NORTH ⇒
        None
      case _ ⇒
        None
    }

  private def y(part: GraphicalEditPart) = part.getFigure.getBounds.y

  private def navigate(currentPart: AttributeNameEditPart, direction: Int, siblingParts: List[GraphicalEditPart]): Option[GraphicalEditPart] =
    direction match {
      case SOUTH ⇒
        siblingParts.collect { case p: AttributeNameEditPart if y(p) > y(currentPart) ⇒ p }.sortBy(y).headOption
      case NORTH ⇒
        siblingParts.collect { case p: AttributeNameEditPart if y(p) < y(currentPart) ⇒ p }.sortBy(-y(_)).headOption orElse
          siblingParts.find(_.isInstanceOf[NodeNameEditPart])
      case EAST ⇒
        siblingParts.collect { case p: AttributeValueEditPart ⇒ p }.sortBy(p ⇒ abs(y(p) - y(currentPart))).headOption
      case _ ⇒
        None
    }

  private def navigate(currentPart: AttributeValueEditPart, direction: Int, siblingParts: List[GraphicalEditPart]): Option[GraphicalEditPart] =
    direction match {
      case SOUTH ⇒
        siblingParts.collect { case p: AttributeValueEditPart if y(p) > y(currentPart) ⇒ p }.sortBy(y).headOption
      case NORTH ⇒
        siblingParts.collect { case p: AttributeValueEditPart if y(p) < y(currentPart) ⇒ p }.sortBy(-y(_)).headOption orElse
          siblingParts.find(_.isInstanceOf[NodeNameEditPart])
      case WEST ⇒
        siblingParts.collect { case p: AttributeNameEditPart ⇒ p }.sortBy(p ⇒ abs(y(p) - y(currentPart))).headOption
      case _ ⇒
        None
    }

}