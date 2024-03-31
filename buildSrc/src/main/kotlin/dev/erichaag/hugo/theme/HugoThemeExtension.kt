package dev.erichaag.hugo.theme

import org.gradle.api.Project
import org.gradle.api.provider.Provider

abstract class HugoThemeExtension(
  private val project: Project,
  private val configurationName: String,
) {
  fun version(version: Provider<String>) {
    project.dependencies.add(configurationName, "dillonzq:LoveIt:${version.get()}@tar.gz")
  }
}
