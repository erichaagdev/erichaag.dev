package dev.erichaag.hugo

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.language.base.plugins.LifecycleBasePlugin

@Suppress("unused")
class HugoPlugin : Plugin<Project> {

  companion object {
    private const val HUGO_CONFIGURATION_NAME = "hugo"
    private const val HUGO_DEFAULT_VERSION = "0.108.0"
    private const val HUGO_EXTENSION_NAME = "hugo"
  }

  override fun apply(project: Project): Unit = with(project) {
    applyBasePlugin()
    registerHugoRepository()
    createHugoExtension()
    createHugoConfiguration()
    configureTasks()
  }

  private fun Project.applyBasePlugin() {
    pluginManager.apply(BasePlugin::class.java)
  }

  private fun Project.registerHugoRepository() {
    with(repositories) {
      exclusiveContent {
        forRepository {
          ivy {
            url = uri("https://github.com/gohugoio/hugo/releases/download")
            patternLayout { artifact("v[revision]/hugo_extended_[revision]_linux-amd64.[ext]") }
            metadataSources { artifact() }
          }
        }
        filter { includeModule("gohugoio", "hugo") }
      }
    }
  }

  private fun Project.createHugoExtension() {
    val hugo = extensions.create<HugoExtension>(HUGO_EXTENSION_NAME)
    hugo.buildDrafts.convention(false)
    hugo.sourceDirectory.convention(layout.projectDirectory.dir("src/hugo"))
    hugo.processDirectory.convention(layout.buildDirectory.dir("hugo/process"))
    hugo.publicDirectory.convention(layout.buildDirectory.dir("hugo/public"))
  }

  private fun Project.createHugoConfiguration() {
    configurations.create(HUGO_CONFIGURATION_NAME) {
      isCanBeConsumed = false
      dependencies.add(project.dependencies.create("gohugoio:hugo:$HUGO_DEFAULT_VERSION@tar.gz"))
    }
  }

  private fun Project.configureTasks() {
    val hugoExtension = extensions.getByType<HugoExtension>()
    val hugoConfiguration = configurations.named(HUGO_CONFIGURATION_NAME)

    val installHugo = tasks.register<HugoInstall>("installHugo") {
      dependsOn(hugoConfiguration)
      fromConfiguration.set(hugoConfiguration)
    }

    val processHugo = tasks.register<HugoProcess>("processHugo") {
      sourceFiles.from(hugoExtension.sourceDirectory)
      outputDirectory.set(hugoExtension.processDirectory)
    }

    val buildHugo = tasks.register<HugoBuild>("buildHugo") {
      dependsOn(processHugo, installHugo)
      binary.set(installHugo.flatMap { it.binary })
      buildDrafts.set(hugoExtension.buildDrafts)
      sourceFiles.setDir(processHugo.flatMap { it.outputDirectory })
      sourceFiles.exclude("resources")
      publicDirectory.set(hugoExtension.publicDirectory)
    }

    tasks.register<HugoServe>("serveHugo") {
      dependsOn(installHugo)
      binary.set(installHugo.flatMap { it.binary })
      sourceDirectory.set(processHugo.flatMap { it.outputDirectory })
    }

    tasks.register<HugoVersion>("printHugoVersion") {
      dependsOn(installHugo)
      binary.set(installHugo.flatMap { it.binary })
    }

    tasks.named(LifecycleBasePlugin.BUILD_TASK_NAME) {
      dependsOn(buildHugo)
    }
  }
}
