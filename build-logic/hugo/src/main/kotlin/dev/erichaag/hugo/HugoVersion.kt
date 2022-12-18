package dev.erichaag.hugo

import dev.erichaag.common.ExecTask
import org.gradle.api.tasks.TaskAction

abstract class HugoVersion : ExecTask, AbstractHugoTask() {

  @TaskAction
  fun action() = binaryExec {
    args("version")
  }
}
