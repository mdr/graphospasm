package com.github.mdr.graphospasm.grapheditor.model
import com.github.mdr.graphospasm.core.graph.Name

object Connection {

  def connect(source: Node, target: Node, initialNameOpt: Option[Name] = None): Connection = {
    val connection = new Connection(source, target, initialNameOpt)
    connection.undelete()
    connection
  }

  def create(source: Node, target: Node, initialNameOpt: Option[Name] = None): Connection =
    new Connection(source, target, initialNameOpt)

}

class Connection private (initialSource: Node, initialTarget: Node, initialNameOpt: Option[Name]) extends Observable {

  private var nameOpt_ : Option[Name] = initialNameOpt

  def nameOpt = nameOpt_

  def nameOpt_=(newNameOpt: Option[Name]) {
    nameOpt_ = newNameOpt
    fireEvent(LocalPropertyChanged)
  }

  private var source_ : Node = initialSource

  def source = source_

  def source_=(node: Node) {
    source_.removeSourceConnection(this)
    source_ = node
    source_.addSourceConnection(this)
  }

  private var target_ : Node = initialTarget

  def target = target_

  def target_=(node: Node) {
    target_.removeTargetConnection(this)
    target_ = node
    target_.addTargetConnection(this)
  }

  var isDeleted: Boolean = true

  def delete() {
    source_.removeSourceConnection(this)
    target_.removeTargetConnection(this)
    isDeleted = true
  }

  def undelete() {
    source_.addSourceConnection(this)
    target_.addTargetConnection(this)
    isDeleted = false
  }

  def copy = Connection.create(source, target, nameOpt)

}