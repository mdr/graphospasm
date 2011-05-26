package com.github.mdr.graphospasm.grapheditor.namespace
import com.github.mdr.graphospasm.core.graph.Name

trait NamespacePrefixManager {

  def getPrefix(name: Name): String

  def getDisplayName(name: Name): String

  def registerPrefix(namespace: String, prefix: String)

  def setShowNamespaces(showNamespaces: Boolean)

}