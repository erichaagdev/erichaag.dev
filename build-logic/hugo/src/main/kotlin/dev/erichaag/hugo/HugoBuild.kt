package dev.erichaag.hugo

import dev.erichaag.common.ExecTask
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class HugoBuild : ExecTask, AbstractHugoTask() {

  @get:InputFiles
  @get:IgnoreEmptyDirectories
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val sourceFiles: ConfigurableFileTree

  @get:Input
  abstract val buildDrafts: Property<Boolean>

  @get:OutputDirectory
  abstract val publicDirectory: DirectoryProperty

  @TaskAction
  fun action() = binaryExec {
    args("--source", sourceFiles.dir.path)
    args("--destination", publicDirectory.path)
    args("--noBuildLock")
    args("--minify")
    if (buildDrafts.get()) args("--buildDrafts")
  }
}
