package com.github.mdr.graphospasm.grapheditor.part
import com.github.mdr.graphospasm.grapheditor.model.Listener
import com.github.mdr.graphospasm.grapheditor.model.LocalPropertyChanged
import com.github.mdr.graphospasm.grapheditor.Plugin

trait SuspendableUpdates { self: Listener ⇒

  def updatesSuspended = Plugin.suspendUpdates

  var hasPendingUpdates_ = false

  def hasPendingUpdates = hasPendingUpdates_

  def flagAsDirty() { hasPendingUpdates_ = true }

  def flushUpdates() {
    hasPendingUpdates_ = false
    changed(LocalPropertyChanged)
  }

}