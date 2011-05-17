package com.github.mdr.graphospasm.grapheditor.model

class AttributeValue(initialValue: AnyRef) extends Observable {

  private var value_ : AnyRef = initialValue

  def value = value_

  def value_=(newValue: AnyRef) {
    value_ = newValue
    fireEvent(LocalPropertyChanged)
  }

  def presentationString =
    if (value_ == null) "null"
    else value_.toString.filterNot(c ⇒ c == '\n' || c == '\r')

}