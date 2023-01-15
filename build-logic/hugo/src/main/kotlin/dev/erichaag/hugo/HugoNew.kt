package dev.erichaag.hugo

import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.provider.Property
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask
import org.gradle.api.tasks.options.Option

@Suppress("UnstableApiUsage")
@UntrackedTask(because = "Not worth tracking")
abstract class HugoNew : AbstractHugoExecTask() {

  @get:InputFiles
  @get:IgnoreEmptyDirectories
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val sourceFiles: ConfigurableFileTree

  @get:Input
  @get:Option(option = "contentPath", description = "Create new content at the specified path.")
  @get:Optional
  abstract val contentPath: Property<String>

  @TaskAction
  fun action() = hugoExec {
    args("new")
    args("--source", sourceFiles.dir.path)
    args("--noBuildLock")
    if (contentPath.isPresent) args(contentPath.get())
  }
}
