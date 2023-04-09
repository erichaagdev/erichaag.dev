@file:Suppress("UnstableApiUsage", "HasPlatformType")

import dev.erichaag.hugo.HugoArtifactTransform
import dev.erichaag.hugo.HugoBuild
import dev.erichaag.hugo.HugoExtension
import dev.erichaag.hugo.HugoServe
import dev.erichaag.hugo.HugoVersion
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE

plugins {
  id("base")
}

val hugoExtension = extensions.create<HugoExtension>("hugo")

val hugoExecutableConfiguration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo-executable")
}

val hugoPost by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("hugo-post"))
}

val hugoTheme by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
}

val hugoBuild by tasks.registering(HugoBuild::class) {
  hugoExecutable = hugoExecutableConfiguration
  publicDirectory = layout.buildDirectory.dir("hugo/public")
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

val hugoVersion by tasks.registering(HugoVersion::class) {
  hugoExecutable = hugoExecutableConfiguration
}

val hugoServe by tasks.registering(HugoServe::class) {
  hugoExecutable = hugoExecutableConfiguration
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

val processHugo by tasks.registering(Sync::class) {
  duplicatesStrategy = DuplicatesStrategy.FAIL
  into(layout.buildDirectory.dir("tasks/$name"))
  into("") {
    from(layout.projectDirectory.dir("hugo"))
    from(hugoPost)
    from(hugoTheme)
  }
}

val build by tasks.getting {
  dependsOn(hugoBuild)
}

dependencies.registerTransform(HugoArtifactTransform::class) {
  from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo-download")
  to.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo-executable")
}
