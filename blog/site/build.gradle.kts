plugins {
  id("dev.erichaag.firebase-deploy")
  id("dev.erichaag.hugo-site")
}

dependencies {
  hugoTheme(projects.blog.theme)
  hugoPost(projects.blog.posts.springInitializrGradleBuildImprovements)
  hugoPost(projects.blog.posts.workAvoidanceWithGradle)
}

hugo {
  toolchainVersion(libs.versions.hugo)
}

firebase {
  toolchainVersion(libs.versions.firebase)
  projectName = "erichaagdev"
}
