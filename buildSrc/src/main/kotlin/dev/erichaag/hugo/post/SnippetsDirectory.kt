package dev.erichaag.hugo.post

import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.OutputDirectory
import org.gradle.process.CommandLineArgumentProvider

class SnippetsDirectory(@get:OutputDirectory val value: Provider<Directory>) : CommandLineArgumentProvider {
  override fun asArguments() = listOf("-Ddev.erichaag.snippetsDirectory=${value.get().asFile.path}")
}
