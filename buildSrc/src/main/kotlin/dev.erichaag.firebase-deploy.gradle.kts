@file:Suppress("UnstableApiUsage")

import dev.erichaag.firebase.FirebaseArtifactTransform
import dev.erichaag.firebase.FirebaseDeploy
import dev.erichaag.firebase.FirebaseExtension
import dev.erichaag.firebase.FirebaseVersion
import dev.erichaag.hugo.HugoBuild
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE

plugins {
  id("base")
}

val firebaseExecutableDeclarable = configurations.dependencyScope("firebaseExecutable") {
  attributes.attribute(ARTIFACT_TYPE_ATTRIBUTE, "firebase-executable")
}.get()

val firebaseExecutableResolvable = configurations.resolvable("${firebaseExecutableDeclarable.name}Resolvable") {
  extendsFrom(firebaseExecutableDeclarable)
  attributes.attribute(ARTIFACT_TYPE_ATTRIBUTE, "firebase-executable")
}

val firebaseExtension = extensions.create<FirebaseExtension>("firebase", firebaseExecutableDeclarable.name)
firebaseExtension.configFile.convention(layout.projectDirectory.file("firebase.json"))
pluginManager.withPlugin("dev.erichaag.hugo-site") {
  firebaseExtension.publicDirectory.convention(tasks.named<HugoBuild>("buildHugoSite").flatMap { it.publicDirectory })
}

dependencies.registerTransform(FirebaseArtifactTransform::class) {
  from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "firebase-download")
  to.attribute(ARTIFACT_TYPE_ATTRIBUTE, "firebase-executable")
}

repositories {
  exclusiveContent {
    forRepository {
      ivy {
        name = "Firebase Releases"
        url = project.uri("https://github.com/firebase/firebase-tools")
        patternLayout { artifact("/releases/download/v[revision]/firebase-tools-[classifier]") }
        metadataSources { artifact() }
      }
    }
    filter { includeModule("firebase", "firebase-tools") }
  }
}

val deployFirebase by tasks.registering(FirebaseDeploy::class) {
  firebaseExecutable = firebaseExecutableResolvable
  projectName = firebaseExtension.projectName
  publicDirectory = firebaseExtension.publicDirectory
  configFile = firebaseExtension.configFile
}

val printFirebaseVersion by tasks.registering(FirebaseVersion::class) {
  firebaseExecutable = firebaseExecutableResolvable
}
