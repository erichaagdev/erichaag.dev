package dev.erichaag.common

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

interface ExecTask {

  @get:InputFile
  @get:PathSensitive(PathSensitivity.RELATIVE)
  val binary: RegularFileProperty

  @get:Inject
  val execOperations: ExecOperations

  fun binaryExec(action: Action<ExecSpec>): ExecResult = execOperations.exec {
    it.commandLine(binary.path)
    action.execute(it)
  }.assertNormalExitValue()

  val FileSystemLocationProperty<*>.path: String
    get() = get().asFile.path
}
