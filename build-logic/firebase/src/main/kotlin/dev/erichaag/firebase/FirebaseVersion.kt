package dev.erichaag.firebase

import dev.erichaag.common.ExecTask
import org.gradle.api.tasks.TaskAction

abstract class FirebaseVersion : ExecTask, AbstractFirebaseTask() {

  @TaskAction
  fun action() = binaryExec {
    args("--version")
  }
}
