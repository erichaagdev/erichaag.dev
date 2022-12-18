package dev.erichaag.hugo

import org.gradle.api.Action
import org.gradle.api.file.FileSystemLocationProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec
import javax.inject.Inject

abstract class AbstractHugoExecTask : AbstractHugoTask() {

  @get:InputFile
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val hugo: RegularFileProperty

  @get:Inject
  abstract val execOperations: ExecOperations

  protected fun exec(action: Action<ExecSpec>): ExecResult = execOperations.exec(action).assertNormalExitValue()

  protected val FileSystemLocationProperty<*>.path: String
    get() = get().asFile.path
}
