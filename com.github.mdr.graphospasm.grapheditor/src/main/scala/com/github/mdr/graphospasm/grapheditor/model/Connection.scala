package com.github.mdr.graphospasm.grapheditor.model

object Connection {

  def connect(source: Node, target: Node): Connection =
    new Connection(source, target)

}

class Connection private (initialSource: Node, initialTarget: Node) {

  private var source_ : Node = initialSource
  private var target_ : Node = initialTarget

  {
    source_.addSourceConnection(this)
    target_.addTargetConnection(this)
  }

  def source = source_
  def target = target_

  def source_=(node: Node) {
    source_.removeSourceConnection(this)
    source_ = node
    source_.addSourceConnection(this)
  }

  def target_=(node: Node) {
    target_.removeTargetConnection(this)
    target_ = node
    target_.addTargetConnection(this)
  }

  def delete() {
    source_.removeSourceConnection(this)
    target_.removeTargetConnection(this)
  }

}