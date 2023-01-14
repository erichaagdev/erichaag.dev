package dev.erichaag.hugo

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class HugoServe : HugoExecTask() {

  @get:InputDirectory
  abstract val sourceDirectory: DirectoryProperty

  @TaskAction
  fun action() = hugoExec {
    args("serve")
    args("--source", sourceDirectory.get().asFile.path)
    args("--disableFastRender")
  }
}
