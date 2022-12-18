package dev.erichaag.hugo

import dev.erichaag.common.ExecTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class HugoServe : ExecTask, AbstractHugoTask() {

  @get:InputDirectory
  abstract val sourceDirectory: DirectoryProperty

  @TaskAction
  fun action() = binaryExec {
    args("serve")
    args("--source", sourceDirectory.path)
    args("--disableFastRender")
  }
}
