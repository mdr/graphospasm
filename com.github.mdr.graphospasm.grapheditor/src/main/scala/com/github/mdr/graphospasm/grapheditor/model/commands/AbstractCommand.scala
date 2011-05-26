package com.github.mdr.graphospasm.grapheditor.model.commands

import org.eclipse.gef.commands.Command

abstract class AbstractCommand extends Command {

  protected type CommandExecutionData

  protected def execute(data: CommandExecutionData)

  protected def undo(data: CommandExecutionData)

  protected def createCommandExecutionData: CommandExecutionData

  protected var dataOpt: Option[CommandExecutionData] = None

  override def execute() = dataOpt match {
    case Some(data) ⇒
      execute(data)
    case None ⇒
      val data = createCommandExecutionData
      dataOpt = Some(data)
      execute(data)
  }

  override def undo() = dataOpt match {
    case Some(data) ⇒ undo(data)
    case None       ⇒ throw new IllegalStateException("undo() cannot occur before execute()")
  }

}