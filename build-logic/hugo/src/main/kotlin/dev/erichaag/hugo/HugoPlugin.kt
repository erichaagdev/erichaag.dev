package dev.erichaag.hugo

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.language.base.plugins.LifecycleBasePlugin

class HugoPlugin : Plugin<Project> {

  companion object {
    private const val HUGO_CONFIGURATION_NAME = "hugo"
    private const val HUGO_DEFAULT_VERSION = "0.108.0"
    private const val HUGO_EXTENSION_NAME = "hugo"
    const val HUGO_GROUP_NAME = "hugo"
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
      archive.set(hugoConfiguration)
    }

    val processHugo = tasks.register<HugoProcess>("processHugo") {
      source.from(layout.projectDirectory.dir("src/hugo"))
      outputDirectory.set(layout.buildDirectory.dir("hugo/source"))
    }

    val buildHugo = tasks.register<HugoBuild>("buildHugo") {
      dependsOn(processHugo, installHugo)
      hugo.set(installHugo.flatMap { it.hugo })
      buildDrafts.set(hugoExtension.buildDrafts)
      source.setDir(processHugo.flatMap { it.outputDirectory })
      source.exclude("resources")
      destination.set(layout.buildDirectory.dir("hugo/public"))
    }

    tasks.register<HugoServe>("serveHugo") {
      dependsOn(installHugo)
      hugo.set(installHugo.flatMap { it.hugo })
      source.set(processHugo.flatMap { it.outputDirectory })
    }

    tasks.register<HugoVersion>("printHugoVersion") {
      dependsOn(installHugo)
      hugo.set(installHugo.flatMap { it.hugo })
    }

    tasks.named(LifecycleBasePlugin.BUILD_TASK_NAME) {
      dependsOn(buildHugo)
    }
  }
}
