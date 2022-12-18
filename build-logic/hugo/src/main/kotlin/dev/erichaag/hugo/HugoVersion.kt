package dev.erichaag.hugo

import org.gradle.api.tasks.TaskAction

abstract class HugoVersion : AbstractHugoExecTask() {

  @TaskAction
  fun action() = exec {
    commandLine(hugo.path, "version")
  }
}
