@file:Suppress("UnstableApiUsage", "HasPlatformType")

import dev.erichaag.firebase.FirebaseArtifactTransform
import dev.erichaag.firebase.FirebaseDeploy
import dev.erichaag.firebase.FirebaseExtension
import dev.erichaag.firebase.FirebaseVersion

plugins {
  id("base")
}

val firebaseExtension = extensions.create<FirebaseExtension>("firebase")
firebaseExtension.configFile.convention(layout.projectDirectory.file("firebase.json"))

val firebaseExecutableConfiguration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "firebase-executable")
}

dependencies.registerTransform(FirebaseArtifactTransform::class) {
  from.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "firebase-download")
  to.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "firebase-executable")
}

val firebaseDeploy by tasks.registering(FirebaseDeploy::class) {
  firebaseExecutable = firebaseExecutableConfiguration
  projectName = firebaseExtension.projectName
  publicDirectory = firebaseExtension.publicDirectory
  configFile = firebaseExtension.configFile
}

val firebaseVersion by tasks.registering(FirebaseVersion::class) {
  firebaseExecutable = firebaseExecutableConfiguration
}
