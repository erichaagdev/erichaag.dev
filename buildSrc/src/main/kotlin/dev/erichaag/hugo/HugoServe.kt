package dev.erichaag.hugo

import javax.inject.Inject
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask
import org.gradle.process.ExecOperations

@UntrackedTask(because = "Not worth tracking")
abstract class HugoServe @Inject constructor(
  execOperations: ExecOperations
) : AbstractHugoTask(execOperations) {

  @get:InputDirectory
  abstract val sourceDirectory: DirectoryProperty

  @TaskAction
  fun action() = hugoExec {
    args("serve")
    args("--source", sourceDirectory.get().asFile.path)
    args("--disableFastRender")
    args("--noBuildLock")
  }
}
