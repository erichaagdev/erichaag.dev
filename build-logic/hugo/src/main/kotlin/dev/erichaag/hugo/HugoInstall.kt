package dev.erichaag.hugo

import dev.erichaag.common.CopyTask
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class HugoInstall @Inject constructor(
  private val archiveOperations: ArchiveOperations,
  projectLayout: ProjectLayout,
) : CopyTask, AbstractHugoTask() {

  companion object {
    private const val HUGO_BINARY_NAME = "hugo"
  }

  @get:InputFiles
  abstract val fromConfiguration: Property<FileCollection>

  @get:Internal
  val intoDirectory: Provider<Directory> = projectLayout.buildDirectory.dir("bin/hugo")

  @get:OutputFile
  val binary: Provider<RegularFile> = intoDirectory.map { it.file(HUGO_BINARY_NAME) }

  @TaskAction
  fun action() = copy {
    from(archiveOperations.tarTree(fromConfiguration.get().singleFile))
    include(HUGO_BINARY_NAME)
    into(intoDirectory)
  }
}
