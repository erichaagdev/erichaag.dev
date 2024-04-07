plugins {
  id("dev.erichaag.firebase-deploy")
  id("dev.erichaag.hugo-site")
}

dependencies {
  hugoPost(projects.blog.posts.bootifulBuildsBestPracticesSpringBootGradle)
  hugoPost(projects.blog.posts.workAvoidanceWithGradle)
}

hugo {
  toolchainVersion(libs.versions.hugo)
}

firebase {
  toolchainVersion(libs.versions.firebase)
  projectName = "erichaagdev"
}
