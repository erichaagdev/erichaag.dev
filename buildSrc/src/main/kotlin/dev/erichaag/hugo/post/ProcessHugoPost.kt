package dev.erichaag.hugo.post

import java.io.File
import javax.inject.Inject
import org.apache.commons.text.StringSubstitutor
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class ProcessHugoPost @Inject constructor(
  private val fileSystemOperations: FileSystemOperations
) : DefaultTask() {

  @get:InputFile
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val index: RegularFileProperty

  @get:Input
  abstract val contentPath: Property<String>

  @get:Input
  abstract val substitutions: MapProperty<String, String>

  @get:InputFiles
  @get:PathSensitive(PathSensitivity.RELATIVE)
  @get:IgnoreEmptyDirectories
  abstract val snippetsDirectory: DirectoryProperty

  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  @TaskAction
  fun action() {
    fileSystemOperations.delete { delete(outputDirectory) }
    val substitutor = StringSubstitutor(substitutions.get(), "#{:", "}")
    val content = substitutor.replace(index.get().asFile.readText())
    outputDirectory.file("content/posts/${contentPath.get()}").get().asFile.run {
      mkdirs()
      resolve("index.md").writeText(content)
    }
    fileSystemOperations.copy {
      from(snippetsDirectory)
      into(outputDirectory.dir("layouts/shortcodes/${contentPath.get()}"))
    }
  }
}
