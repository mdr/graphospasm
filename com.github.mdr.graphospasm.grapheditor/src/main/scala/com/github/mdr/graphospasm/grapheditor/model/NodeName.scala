package com.github.mdr.graphospasm.grapheditor.model

import com.github.mdr.graphospasm.core.graph.Name
import org.eclipse.draw2d.geometry._

class NodeName(initialName: Name) extends Observable {

  private var name_ : Name = initialName

  def name = name_

  def name_=(newName: String) {
    name_ = name_.copy(simpleName = newName)
    fireEvent(LocalPropertyChanged)
  }
  //  private var bounds_ = new Rectangle(0, 0, 0, 0)
  //
  //  def bounds = bounds_.getCopy
  //
  //  def bounds_=(newBounds: Rectangle) {
  //    bounds_ = newBounds.getCopy
  //    fireEvent(LocalPropertyChanged)
  //  }
  //
  //  def width = size.width
  //  def height = size.height
  //  def x = bounds_.x
  //  def y = bounds_.y
  //
  //  def size = bounds.getSize
  //
  //  def size_=(dimension: Dimension) {
  //    bounds_ = bounds_.getCopy.setSize(dimension)
  //    fireEvent(LocalPropertyChanged)
  //  }

}