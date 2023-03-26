@file:Suppress("UnstableApiUsage")

plugins {
  id("dev.erichaag.firebase")
  id("dev.erichaag.blog")
}

dependencies {
  blogTheme(projects.blog.theme)
  blogPost(projects.blog.posts.workAvoidanceWithGradle)
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
