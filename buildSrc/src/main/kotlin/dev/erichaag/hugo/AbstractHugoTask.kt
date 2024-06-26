package dev.erichaag.hugo

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.process.ExecOperations
import org.gradle.process.ExecSpec
import javax.inject.Inject

abstract class AbstractHugoTask @Inject constructor(
  private val execOperations: ExecOperations
) : DefaultTask() {

  @get:PathSensitive(PathSensitivity.NONE)
  @get:InputFiles
  abstract val hugoExecutable: Property<FileCollection>

  init {
    group = "hugo"
  }

  fun hugoExec(action: Action<ExecSpec>) = execOperations.exec {
    commandLine(hugoExecutable.get().singleFile.path)
    action.execute(this)
  }.assertNormalExitValue()!!
}
