package dev.erichaag.hugo

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE
import org.gradle.api.attributes.Category
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.registerTransform

class HugoPlugin : Plugin<Project> {

  override fun apply(project: Project) = with(project) {
    applyBasePlugin()
    createHugoConfiguration()
    createHugoExtension()
    registerHugoArtifactTransform()
  }

  private fun Project.applyBasePlugin() {
    pluginManager.apply(BasePlugin::class.java)
  }

  private fun Project.createHugoConfiguration() {
    configurations.create("hugoArtifact") {
      isCanBeConsumed = false
      isCanBeResolved = true
      attributes.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo")
    }

    configurations.create("hugoContent") {
      isCanBeConsumed = false
      isCanBeResolved = true
      attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-content"))
    }

    configurations.create("hugoContentElements") {
      isCanBeConsumed = true
      isCanBeResolved = false
      attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-content"))
    }

    configurations.create("hugoTheme") {
      isCanBeConsumed = false
      isCanBeResolved = true
      attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
    }

    configurations.create("hugoThemeElements") {
      isCanBeConsumed = true
      isCanBeResolved = false
      attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
    }
  }

  private fun Project.createHugoExtension() {
    extensions.create<HugoExtension>("hugo")
  }

  private fun Project.registerHugoArtifactTransform() {
    dependencies.registerTransform(HugoArtifactTransform::class) {
      from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "zip")
      to.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo")
    }

    dependencies.registerTransform(HugoArtifactTransform::class) {
      from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "tar.gz")
      to.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo")
    }
  }
}
