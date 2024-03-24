package dev.erichaag.hugo.theme

import org.gradle.api.Project

abstract class HugoThemeExtension(private val project: Project) {
  fun version(version: String) {
    project.dependencies.add("loveItTheme", "dillonzq:LoveIt:$version@tar.gz")
  }
}
