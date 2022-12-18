@file:Suppress("unused")

package dev.erichaag.firebase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register

class FirebasePlugin : Plugin<Project> {

  companion object {
    private const val FIREBASE_CONFIGURATION_NAME = "firebase"
    private const val FIREBASE_DEFAULT_VERSION = "11.18.0"
    private const val FIREBASE_EXTENSION_NAME = "firebase"
  }

  override fun apply(project: Project) = with(project) {
    applyBasePlugin()
    registerFirebaseRepository()
    createFirebaseExtension()
    createFirebaseConfiguration()
    configureTasks()
  }

  private fun Project.applyBasePlugin() {
    pluginManager.apply(BasePlugin::class.java)
  }

  private fun Project.registerFirebaseRepository() {
    with(repositories) {
      exclusiveContent {
        forRepository {
          ivy {
            url = uri("https://github.com/firebase/firebase-tools/releases/download")
            patternLayout { artifact("v[revision]/firebase-tools-linux") }
            metadataSources { artifact() }
          }
        }
        filter { includeModule("firebase", "firebase-tools") }
      }
    }
  }

  private fun Project.createFirebaseExtension() {
    extensions.create<FirebaseExtension>(FIREBASE_EXTENSION_NAME)
  }

  private fun Project.createFirebaseConfiguration() {
    configurations.create(FIREBASE_CONFIGURATION_NAME) {
      isCanBeConsumed = false
      dependencies.add(project.dependencies.create("firebase:firebase-tools:$FIREBASE_DEFAULT_VERSION"))
    }
  }

  private fun Project.configureTasks() {
    val firebaseConfiguration = configurations.named(FIREBASE_CONFIGURATION_NAME)
    val firebaseExtension = extensions.getByType<FirebaseExtension>()

    val installFirebase = tasks.register<FirebaseInstall>("installFirebase") {
      dependsOn(firebaseConfiguration)
      this.fromConfiguration.set(firebaseConfiguration)
    }

    tasks.register<FirebaseDeploy>("deployFirebase") {
      dependsOn(installFirebase)
      binary.set(installFirebase.flatMap { it.binary })
      projectName.set(firebaseExtension.projectName)
      publicDirectory.set(firebaseExtension.publicDirectory)
    }

    tasks.register<FirebaseVersion>("printFirebaseVersion") {
      dependsOn(installFirebase)
      binary.set(installFirebase.flatMap { it.binary })
    }
  }
}
