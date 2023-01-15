package dev.erichaag.hugo

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask

@Suppress("UnstableApiUsage")
@UntrackedTask(because = "Not worth tracking")
abstract class HugoVersion : AbstractHugoExecTask() {

  @TaskAction
  fun action() = hugoExec {
    args("version")
  }
}
