package com.github.mdr.graphospasm.grapheditor.model

import com.github.mdr.graphospasm.core.graph.Name

class NodeName(initialName: Name) extends Observable {

  private var name_ : Name = initialName

  def name = name_

  def name_=(newName: String) {
    name_ = name_.copy(simpleName = newName)
    fireEvent(LocalPropertyChanged)
  }

}