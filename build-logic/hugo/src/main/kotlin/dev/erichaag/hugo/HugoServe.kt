package dev.erichaag.hugo

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class HugoServe : AbstractHugoExecTask() {

  @get:InputDirectory
  abstract val source: DirectoryProperty

  @TaskAction
  fun action() = exec {
    commandLine(hugo.path, "serve")
    args("--source", source.path)
    args("--disableFastRender")
  }
}
