package dev.erichaag.firebase

import org.gradle.api.tasks.TaskAction

abstract class FirebaseVersion : AbstractFirebaseTask() {

  @TaskAction
  fun action() = firebaseExec {
    args("--version")
  }
}
