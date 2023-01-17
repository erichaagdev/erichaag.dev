package dev.erichaag.hugo.bundle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Category
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

class HugoBundlePlugin : Plugin<Project> {

  override fun apply(project: Project) = with(project) {
    configurations.create("hugoBundleElements") {
      isCanBeConsumed = true
      isCanBeResolved = false
      attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-bundle"))
    }

    val hugoBundle = tasks.register<HugoBundle>("hugoBundle") {
      index.set(layout.projectDirectory.file("index.md"))
      bundleName.set(project.name)
      outputDirectory.set(layout.buildDirectory.dir("hugo/hugoBundle"))
    }

    artifacts {
      add("hugoBundleElements", hugoBundle)
    }
  }
}
