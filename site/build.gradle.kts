@file:Suppress("UnstableApiUsage")

import dev.erichaag.hugo.HugoBuild
import dev.erichaag.hugo.HugoServe
import dev.erichaag.hugo.HugoVersion

plugins {
  id("dev.erichaag.firebase")
  id("dev.erichaag.hugo")
}

dependencies {
  hugoTheme(projects.theme)
  hugoContent(projects.posts.workAvoidanceWithGradle)
}

val processHugo by tasks.registering(Sync::class) {
  duplicatesStrategy = DuplicatesStrategy.FAIL

  into(layout.buildDirectory.dir("hugo/processHugo"))
  into("") {
    from(layout.projectDirectory.dir("hugo"))
    from(configurations.hugoContent)
    from(configurations.hugoTheme)
  }
}

val hugoBuild by tasks.registering(HugoBuild::class) {
  hugo = configurations.hugoArtifact
  publicDirectory = layout.buildDirectory.dir("hugo/public")
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

val hugoVersion by tasks.registering(HugoVersion::class) {
  hugo = configurations.hugoArtifact
}

val hugoServe by tasks.registering(HugoServe::class) {
  hugo = configurations.hugoArtifact
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

tasks.build.configure {
  dependsOn(hugoBuild)
}

hugo {
  releasesRepository()
  toolchainVersion("0.109.0")
}

firebase {
  projectName = "erichaagdev"
  publicDirectory = hugoBuild.flatMap { it.publicDirectory }
  releasesRepository()
  toolchainVersion("11.18.0")
}
