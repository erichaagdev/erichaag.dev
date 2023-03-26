package dev.erichaag.hugo

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.tasks.*
import javax.inject.Inject

@CacheableTask
abstract class HugoBuild @Inject constructor(
  private val fileSystemOperations: FileSystemOperations,
) : AbstractHugoTask() {

  @get:IgnoreEmptyDirectories
  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val sourceDirectory: DirectoryProperty

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
    }
  }
}
