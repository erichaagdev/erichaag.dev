package dev.erichaag.hugo

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask

@UntrackedTask(because = "Not worth tracking")
abstract class HugoVersion : AbstractHugoTask() {

  @TaskAction
  fun action() = hugoExec {
    args("version")
  }
}
