package dev.erichaag.firebase

import org.gradle.api.Action
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec
import javax.inject.Inject

abstract class AbstractFirebaseExecTask : AbstractFirebaseTask() {

  @get:PathSensitive(PathSensitivity.NONE)
  @get:InputFiles
  abstract val firebase: Property<FileCollection>

  @get:Inject
  abstract val execOperations: ExecOperations

  fun firebaseExec(action: Action<ExecSpec>): ExecResult = execOperations.exec {
    commandLine(firebase.get().singleFile.path)
    action.execute(this)
  }.assertNormalExitValue()
}
