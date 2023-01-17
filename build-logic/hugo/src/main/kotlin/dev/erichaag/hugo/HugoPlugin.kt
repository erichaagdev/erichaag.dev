package dev.erichaag.hugo

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.attributes.Category
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registerTransform
import org.gradle.language.base.plugins.LifecycleBasePlugin

class HugoPlugin : Plugin<Project> {

  companion object {
    private const val HUGO_CONFIGURATION_NAME = "hugo"
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
    registerTarArtifactType()
    configureTasks()
  }

  private fun Project.applyBasePlugin() {
    pluginManager.apply(BasePlugin::class.java)
  }

  private fun Project.createHugoConfiguration() {
    configurations.create(HUGO_CONFIGURATION_NAME) {
      isCanBeConsumed = false
      isCanBeResolved = true
      attributes.attribute(ARTIFACT_TYPE_KEY, ARTIFACT_TYPE_VALUE)
    }

    configurations.create("hugoBundle") {
      isCanBeConsumed = false
      isCanBeResolved = true
      attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-bundle"))
    }

    configurations.create("hugoTheme") {
      isCanBeConsumed = false
      isCanBeResolved = true
    }
  }

  private fun Project.createHugoExtension() {
    val hugo = extensions.create<HugoExtension>(HUGO_EXTENSION_NAME, repositories)
    hugo.sourceDirectory.convention(layout.projectDirectory.dir("hugo"))
    hugo.publicDirectory.convention(layout.buildDirectory.dir("hugo/public"))
  }

  private fun Project.registerHugoArtifactTransform() {
    dependencies.registerTransform(HugoArtifactTransform::class) {
      from.attribute(ARTIFACT_TYPE_KEY, "tar.gz")
      to.attribute(ARTIFACT_TYPE_KEY, ARTIFACT_TYPE_VALUE)
    }
  }

  private fun Project.registerTarArtifactType() {
    if (dependencies.artifactTypes.findByName("tar.gz") == null) {
      dependencies.artifactTypes.register("tar.gz") {
        attributes.attribute(ARTIFACT_TYPE_KEY, "tar.gz")
      }
    }
  }

  private fun Project.configureTasks() {
    val hugoExtension = extensions.getByType<HugoExtension>()
    val hugoConfiguration = configurations.named(HUGO_CONFIGURATION_NAME)

    val buildHugo = tasks.register<HugoBuild>("hugoBuild") {
      dependsOn(hugoConfiguration)
      hugo.set(hugoConfiguration)
      publicDirectory.set(hugoExtension.publicDirectory)
      sourceDirectory.set(hugoExtension.sourceDirectory)
    }

    tasks.register<HugoServe>("hugoServe") {
      dependsOn(hugoConfiguration)
      hugo.set(hugoConfiguration)
      sourceDirectory.set(hugoExtension.sourceDirectory)
    }

    tasks.register<HugoVersion>("hugoVersion") {
      dependsOn(hugoConfiguration)
      hugo.set(hugoConfiguration)
    }

    tasks.named(LifecycleBasePlugin.BUILD_TASK_NAME) {
      dependsOn(buildHugo)
    }
  }
}
