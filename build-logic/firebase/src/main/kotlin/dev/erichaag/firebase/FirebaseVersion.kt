package dev.erichaag.firebase

import org.gradle.api.tasks.TaskAction

abstract class FirebaseVersion : AbstractFirebaseExecTask() {

  @TaskAction
  fun action() = firebaseExec {
    args("--version")
  }
}
