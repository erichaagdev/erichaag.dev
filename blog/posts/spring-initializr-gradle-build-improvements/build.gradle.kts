plugins {
  id("dev.erichaag.hugo-post")
}

hugoPost {
  substitute("commonCustomUserDataGradlePluginVersion" to libs.versions.commonCustomUserDataGradlePlugin)
  substitute("develocityGradlePluginVersion" to libs.versions.develocityGradlePlugin)
  substitute("gradleVersion" to GradleVersion.current().version)
  substitute("springBootVersion" to libs.versions.springBoot)
  substitute("springCloudVersion" to libs.versions.springCloud)
  substitute("springDependencyManagementGradlePlugin" to libs.versions.springDependencyManagementGradlePlugin)
}
