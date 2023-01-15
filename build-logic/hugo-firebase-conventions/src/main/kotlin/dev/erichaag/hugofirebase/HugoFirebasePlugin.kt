package dev.erichaag.hugofirebase

import dev.erichaag.firebase.FirebaseExtension
import dev.erichaag.hugo.HugoBuild
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

class HugoFirebasePlugin : Plugin<Project> {

  override fun apply(project: Project) = with(project) {
    pluginManager.withPlugin("dev.erichaag.hugo") {
      val hugoBuild = tasks.named("hugoBuild", HugoBuild::class)
      pluginManager.withPlugin("dev.erichaag.firebase") {
        val firebase = extensions.getByType<FirebaseExtension>()
        firebase.publicDirectory.convention(hugoBuild.flatMap { it.publicDirectory })
      }
    }
  }
}
