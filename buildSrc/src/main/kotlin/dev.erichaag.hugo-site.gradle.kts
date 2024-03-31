@file:Suppress("UnstableApiUsage")

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

val hugoExecutableDeclarable = configurations.dependencyScope("hugoExecutable") {
  attributes.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo-executable")
}.get()

val hugoExecutableResolvable = configurations.resolvable("${hugoExecutableDeclarable.name}Resolvable") {
  extendsFrom(hugoExecutableDeclarable)
  attributes.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo-executable")
}

val hugoExtension = extensions.create<HugoExtension>("hugo", hugoExecutableDeclarable.name)

dependencies.registerTransform(HugoArtifactTransform::class) {
  from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo-download")
  to.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo-executable")
}

val hugoPostDeclarable = configurations.dependencyScope("hugoPost") {
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("hugo-post"))
}.get()

val hugoPostResolvable = configurations.resolvable("${hugoPostDeclarable.name}Resolvable") {
  extendsFrom(hugoPostDeclarable)
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("hugo-post"))
}

val hugoThemeDeclarable = configurations.dependencyScope("hugoTheme") {
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
}.get()

val hugoThemeResolvable = configurations.resolvable("${hugoThemeDeclarable.name}Resolvable") {
  extendsFrom(hugoThemeDeclarable)
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
}

val buildHugoSite by tasks.registering(HugoBuild::class) {
  hugoExecutable = hugoExecutableResolvable
  publicDirectory = layout.buildDirectory.dir("hugo/public")
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

val printHugoVersion by tasks.registering(HugoVersion::class) {
  hugoExecutable = hugoExecutableResolvable
}

val serveHugoSite by tasks.registering(HugoServe::class) {
  hugoExecutable = hugoExecutableResolvable
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

repositories {
  exclusiveContent {
    forRepository {
      ivy {
        name = "Hugo Releases"
        url = project.uri("https://github.com/gohugoio/hugo")
        patternLayout { artifact("/releases/download/v[revision]/hugo_extended_[revision]_[classifier]") }
        metadataSources { artifact() }
      }
    }
    filter { includeModule("gohugoio", "hugo") }
  }
}

val processHugo by tasks.registering(Sync::class) {
  duplicatesStrategy = DuplicatesStrategy.FAIL
  into(layout.buildDirectory.dir("tasks/$name"))
  into("") {
    from(layout.projectDirectory.dir("hugo"))
    from(hugoPostResolvable)
    from(hugoThemeResolvable)
  }
}

val build by tasks.getting(DefaultTask::class) {
  dependsOn(buildHugoSite)
}
