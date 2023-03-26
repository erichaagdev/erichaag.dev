@file:Suppress("UnstableApiUsage")

plugins {
  id("dev.erichaag.firebase")
  id("dev.erichaag.blog")
}

dependencies {
  blogTheme(projects.theme)
  blogPost(projects.posts.workAvoidanceWithGradle)
}

hugo {
  releasesRepository()
  toolchainVersion("0.109.0")
}

firebase {
  projectName = "erichaagdev"
  publicDirectory = tasks.hugoBuild.flatMap { it.publicDirectory }
  releasesRepository()
  toolchainVersion("11.18.0")
}
