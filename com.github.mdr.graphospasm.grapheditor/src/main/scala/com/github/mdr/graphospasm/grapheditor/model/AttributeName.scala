package com.github.mdr.graphospasm.grapheditor.model

import com.github.mdr.graphospasm.core.graph.Name

class AttributeName(initialName: Name) extends Observable {

  private var name_ : Name = initialName

  def name = name_

  def name_=(newName: Name) {
    name_ = newName
    fireEvent(LocalPropertyChanged)
  }

  override def toString = getClass.getSimpleName + "(" + name_ + ")"

}