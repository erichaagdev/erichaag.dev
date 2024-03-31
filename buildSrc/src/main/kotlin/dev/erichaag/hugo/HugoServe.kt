package dev.erichaag.hugo

import javax.inject.Inject
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask
import org.gradle.api.tasks.options.Option
import org.gradle.process.ExecOperations

@UntrackedTask(because = "Not worth tracking")
abstract class HugoServe @Inject constructor(
  execOperations: ExecOperations
) : AbstractHugoTask(execOperations) {

  @get:InputDirectory
  abstract val sourceDirectory: DirectoryProperty

  @get:Input
  @get:Option(option = "build-drafts", description = "Includes content marked as draft.")
  abstract val buildDrafts: Property<Boolean>

  @TaskAction
  fun action() = hugoExec {
    args("serve")
    args("--source", sourceDirectory.get().asFile.path)
    args("--disableFastRender")
    args("--noBuildLock")
    if (buildDrafts.get()) args("--buildDrafts")
  }
}
