package dev.erichaag.hugo

import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.OutputDirectory
import org.gradle.process.CommandLineArgumentProvider

class ShortcodesDirectoryArgumentProvider(
  @get:OutputDirectory
  val shortcodesDirectory: Provider<Directory>
) : CommandLineArgumentProvider {

  override fun asArguments() = listOf("-Ddev.erichaag.hugo.shortcodesDirectory=${shortcodesDirectory.get().asFile.path}")
}
