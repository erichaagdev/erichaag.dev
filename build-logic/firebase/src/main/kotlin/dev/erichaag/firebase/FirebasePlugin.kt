@file:Suppress("unused")

package dev.erichaag.firebase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registerTransform

class FirebasePlugin : Plugin<Project> {

  companion object {
    private const val FIREBASE_CONFIGURATION_NAME = "firebase"
    private const val FIREBASE_EXTENSION_NAME = "firebase"

    @Suppress("UnstableApiUsage")
    private val ARTIFACT_TYPE_KEY = ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE
    private const val ARTIFACT_TYPE_VALUE = "firebase"
  }

  override fun apply(project: Project) = with(project) {
    applyBasePlugin()
    createFirebaseExtension()
    createFirebaseConfiguration()
    registerFirebaseArtifactTransform()
    configureTasks()
  }

  private fun Project.applyBasePlugin() {
    pluginManager.apply(BasePlugin::class.java)
  }

  private fun Project.createFirebaseExtension() {
    val firebaseExtension = extensions.create<FirebaseExtension>(FIREBASE_EXTENSION_NAME)
    firebaseExtension.configFile.convention(layout.projectDirectory.file("firebase.json"))
  }

  private fun Project.createFirebaseConfiguration() {
    configurations.create(FIREBASE_CONFIGURATION_NAME) {
      isCanBeConsumed = false
      attributes.attribute(ARTIFACT_TYPE_KEY, ARTIFACT_TYPE_VALUE)
    }
  }

  private fun Project.registerFirebaseArtifactTransform() {
    dependencies.registerTransform(FirebaseArtifactTransform::class) {
      @Suppress("UnstableApiUsage")
      from.attribute(ARTIFACT_TYPE_KEY, ArtifactTypeDefinition.BINARY_DATA_TYPE)
      to.attribute(ARTIFACT_TYPE_KEY, ARTIFACT_TYPE_VALUE)
    }
  }

  private fun Project.configureTasks() {
    val firebaseConfiguration = configurations.named(FIREBASE_CONFIGURATION_NAME)
    val firebaseExtension = extensions.getByType<FirebaseExtension>()

    tasks.register<FirebaseDeploy>("firebaseDeploy") {
      dependsOn(firebaseConfiguration)
      firebase.set(firebaseConfiguration)
      projectName.set(firebaseExtension.projectName)
      publicDirectory.set(firebaseExtension.publicDirectory)
      configFile.set(firebaseExtension.configFile)
    }

    tasks.register<FirebaseVersion>("firebaseVersion") {
      dependsOn(firebaseConfiguration)
      firebase.set(firebaseConfiguration)
    }
  }
}
