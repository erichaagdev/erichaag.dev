package dev.erichaag.hugo

import javax.inject.Inject
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask
import org.gradle.process.ExecOperations

@UntrackedTask(because = "Not worth tracking")
abstract class HugoVersion @Inject constructor(
  execOperations: ExecOperations
) : AbstractHugoTask(execOperations) {

  @TaskAction
  fun action() = hugoExec {
    args("version")
  }
}
