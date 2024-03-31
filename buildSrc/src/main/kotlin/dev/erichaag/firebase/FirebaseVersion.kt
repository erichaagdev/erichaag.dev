package dev.erichaag.firebase

import javax.inject.Inject
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask
import org.gradle.process.ExecOperations

@UntrackedTask(because = "Not worth tracking")
abstract class FirebaseVersion @Inject constructor(
  execOperations: ExecOperations
) : AbstractFirebaseTask(execOperations) {

  @TaskAction
  fun action() = firebaseExec {
    args("--version")
  }
}
