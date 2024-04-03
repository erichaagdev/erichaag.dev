plugins {
  id("dev.erichaag.firebase-deploy")
  id("dev.erichaag.hugo-site")
}

dependencies {
  hugoTheme(projects.blog.theme)
  hugoPost(projects.blog.posts.bootifulBuildsGradleTipsForSpringBoot)
  hugoPost(projects.blog.posts.workAvoidanceWithGradle)
}

hugo {
  toolchainVersion(libs.versions.hugo)
}

firebase {
  toolchainVersion(libs.versions.firebase)
  projectName = "erichaagdev"
}
