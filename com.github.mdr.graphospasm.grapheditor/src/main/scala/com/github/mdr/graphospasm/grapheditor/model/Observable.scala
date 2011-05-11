package com.github.mdr.graphospasm.grapheditor.model

trait Observable {

  private var listeners: List[Listener] = Nil

  protected def fireEvent(event: Event) = listeners foreach { _.changed(event) }

  def addListener(listener: Listener) { listeners ::= listener }

  def removeListener(listener: Listener) { listeners = listeners filterNot { _ == listener } }

}

trait Listener {

  def changed(event: Event)

}

sealed trait Event

case class NodeInserted(node: Node, index: Int) extends Event
case class NodeRemoved(node: Node, index: Int) extends Event
case object LocalPropertyChanged extends Event
