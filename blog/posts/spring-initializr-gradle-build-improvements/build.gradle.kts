plugins {
  id("dev.erichaag.hugo-post")
}

hugoPost {
  substitute("commonCustomUserDataGradlePluginVersion" to libs.versions.commonCustomUserData)
  substitute("develocityGradlePluginVersion" to libs.versions.develocity)
  substitute("gradleVersion" to GradleVersion.current().version)
  substitute("springBootVersion" to libs.versions.springBoot)
  substitute("springCloudVersion" to libs.versions.springCloud)
  substitute("springDependencyManagementGradlePlugin" to libs.versions.springDependencyManagement)
}
