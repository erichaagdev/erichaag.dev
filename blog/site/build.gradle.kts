@file:Suppress("UnstableApiUsage")

plugins {
  id("dev.erichaag.firebase")
  id("dev.erichaag.hugo")
}

dependencies {
  hugoTheme(projects.blog.theme)
  hugoPost(projects.blog.posts.workAvoidanceWithGradle)
}

hugo {
  releasesRepository()
  toolchainVersion("0.109.0")
}

firebase {
  releasesRepository()
  toolchainVersion("11.18.0")
  projectName = "erichaagdev"
  publicDirectory = tasks.hugoBuild.flatMap { it.publicDirectory }
}
