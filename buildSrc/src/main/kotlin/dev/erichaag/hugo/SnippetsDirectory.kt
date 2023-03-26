package dev.erichaag.hugo

import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.OutputDirectory
import org.gradle.process.CommandLineArgumentProvider

class SnippetsDirectory(@get:OutputDirectory val value: Provider<Directory>) : CommandLineArgumentProvider {
  override fun asArguments() = listOf("-Ddev.erichaag.post.snippets=${value.get().asFile.path}")
}