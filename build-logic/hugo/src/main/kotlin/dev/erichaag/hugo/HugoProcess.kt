package dev.erichaag.hugo

import dev.erichaag.common.CopyTask
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class HugoProcess @Inject constructor(
  private val archiveOperations: ArchiveOperations
) : CopyTask, AbstractHugoTask() {

  @get:InputFiles
  @get:IgnoreEmptyDirectories
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val sourceFiles: ConfigurableFileCollection

  @get:InputFiles
  @get:IgnoreEmptyDirectories
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val themeConfiguration: Property<FileCollection>

  @get:Input
  abstract val themeName: Property<String>

  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  @TaskAction
  fun action() {
    copy {
      from(sourceFiles)
      into(outputDirectory)
    }
    copy {
      val themeFile = themeConfiguration.get().singleFile
      from(archiveOperations.tarTree(themeFile))
      exclude("pax_global_header")
      into(outputDirectory.dir("themes"))
      includeEmptyDirs = false
      eachFile {
        path = path.replaceFirst(themeFile.name.removeSuffix(".tar.gz"), themeName.get())
      }
    }
  }
}
