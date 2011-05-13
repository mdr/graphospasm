package com.github.mdr.graphospasm.grapheditor.model

class AttributeValue(initialValue: AnyRef) extends Observable {

  private var value_ : AnyRef = initialValue

  def value = value_

  def name_=(newValue: AnyRef) {
    value_ = newValue
    fireEvent(LocalPropertyChanged)
  }

}