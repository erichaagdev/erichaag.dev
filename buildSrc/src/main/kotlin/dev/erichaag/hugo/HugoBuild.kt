package dev.erichaag.hugo

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import javax.inject.Inject
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.process.ExecOperations

@CacheableTask
abstract class HugoBuild @Inject constructor(
  private val fileSystemOperations: FileSystemOperations,
  execOperations: ExecOperations,
) : AbstractHugoTask(execOperations) {

  @get:IgnoreEmptyDirectories
  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val sourceDirectory: DirectoryProperty

  @get:Input
  @get:Option(option = "build-drafts", description = "Includes content marked as draft.")
  abstract val buildDrafts: Property<Boolean>

  @get:OutputDirectory
  abstract val publicDirectory: DirectoryProperty

  @TaskAction
  fun action() {
    fileSystemOperations.delete {
      delete(publicDirectory)
    }
    hugoExec {
      args("--source", sourceDirectory.get().asFile.path)
      args("--destination", publicDirectory.get().asFile.path)
      args("--noBuildLock")
      args("--minify")
      if (buildDrafts.get()) args("--buildDrafts")
    }
  }
}
