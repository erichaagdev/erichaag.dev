package dev.erichaag.hugo

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
abstract class HugoBuild : AbstractHugoExecTask() {

  @get:InputFiles
  @get:IgnoreEmptyDirectories
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val sourceFiles: ConfigurableFileTree

  @get:Input
  abstract val themeName: Property<String>

  @get:Input
  abstract val buildDrafts: Property<Boolean>

  @get:OutputDirectory
  abstract val publicDirectory: DirectoryProperty

  @TaskAction
  fun action() = hugoExec {
    args("--source", sourceFiles.dir.path)
    args("--destination", publicDirectory.get().asFile.path)
    args("--noBuildLock")
    args("--minify")
    args("--theme", themeName.get())
    if (buildDrafts.get()) args("--buildDrafts")
  }
}
