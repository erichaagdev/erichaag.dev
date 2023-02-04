package dev.erichaag.hugo

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.attributes.Category
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.registerTransform

class HugoPlugin : Plugin<Project> {

  companion object {
    private const val HUGO_ARTIFACT_CONFIGURATION_NAME = "hugoArtifact"
    private const val HUGO_EXTENSION_NAME = "hugo"

    @Suppress("UnstableApiUsage")
    private val ARTIFACT_TYPE_KEY = ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE
    private const val ARTIFACT_TYPE_VALUE = "hugo"
  }

  override fun apply(project: Project) = with(project) {
    applyBasePlugin()
    createHugoConfiguration()
    createHugoExtension()
    registerHugoArtifactTransform()
    registerHugoArtifactType()
  }

  private fun Project.applyBasePlugin() {
    pluginManager.apply(BasePlugin::class.java)
  }

  private fun Project.createHugoConfiguration() {
    configurations.create(HUGO_ARTIFACT_CONFIGURATION_NAME) {
      isCanBeConsumed = false
      isCanBeResolved = true
      attributes.attribute(ARTIFACT_TYPE_KEY, ARTIFACT_TYPE_VALUE)
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
    extensions.create<HugoExtension>(HUGO_EXTENSION_NAME)
  }

  private fun Project.registerHugoArtifactTransform() {
    dependencies.registerTransform(HugoArtifactTransform::class) {
      from.attribute(ARTIFACT_TYPE_KEY, HUGO_ARTIFACT_CONFIGURATION_NAME)
      to.attribute(ARTIFACT_TYPE_KEY, ARTIFACT_TYPE_VALUE)
    }
  }

  private fun Project.registerHugoArtifactType() {
    if (dependencies.artifactTypes.findByName(HUGO_ARTIFACT_CONFIGURATION_NAME) == null) {
      dependencies.artifactTypes.register(HUGO_ARTIFACT_CONFIGURATION_NAME) {
        attributes.attribute(ARTIFACT_TYPE_KEY, HUGO_ARTIFACT_CONFIGURATION_NAME)
      }
    }
  }
}
