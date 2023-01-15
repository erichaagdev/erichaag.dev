package dev.erichaag.hugo

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registerTransform
import org.gradle.language.base.plugins.LifecycleBasePlugin

class HugoPlugin : Plugin<Project> {

  companion object {
    private const val HUGO_CONFIGURATION_NAME = "hugo"
    private const val HUGO_THEME_CONFIGURATION_NAME = "hugoTheme"
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
      attributes.attribute(ARTIFACT_TYPE_KEY, ARTIFACT_TYPE_VALUE)
    }

    configurations.create(HUGO_THEME_CONFIGURATION_NAME) {
      isCanBeConsumed = false
    }
  }

  private fun Project.createHugoExtension() {
    val hugo = extensions.create<HugoExtension>(HUGO_EXTENSION_NAME, repositories)
    hugo.buildDrafts.convention(false)
    hugo.sourceDirectory.convention(layout.projectDirectory.dir("hugo"))
    hugo.processDirectory.convention(layout.buildDirectory.dir("hugo/process"))
    hugo.publicDirectory.convention(layout.buildDirectory.dir("hugo/public"))
    hugo.theme.convention(configurations.named(HUGO_THEME_CONFIGURATION_NAME))
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

    val processHugo = tasks.register<HugoProcess>("hugoProcess") {
      outputDirectory.set(hugoExtension.processDirectory)
      sourceFiles.from(hugoExtension.sourceDirectory)
      theme.set(hugoExtension.theme)
      themeName.set(hugoExtension.themeName)
    }

    val buildHugo = tasks.register<HugoBuild>("hugoBuild") {
      dependsOn(processHugo, hugoConfiguration)
      buildDrafts.set(hugoExtension.buildDrafts)
      hugo.set(hugoConfiguration)
      publicDirectory.set(hugoExtension.publicDirectory)
      sourceFiles.exclude("resources")
      sourceFiles.setDir(processHugo.flatMap { it.outputDirectory })
      themeName.set(hugoExtension.themeName)
    }

    tasks.register<HugoServe>("hugoServe") {
      dependsOn(hugoConfiguration)
      hugo.set(hugoConfiguration)
      sourceDirectory.set(processHugo.flatMap { it.outputDirectory })
      themeName.set(hugoExtension.themeName)
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
