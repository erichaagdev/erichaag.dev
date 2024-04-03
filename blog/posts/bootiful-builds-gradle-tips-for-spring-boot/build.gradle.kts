plugins {
  id("dev.erichaag.hugo-post")
  id("dev.erichaag.gradle-properties")
}

hugoPost {
  substitute("commonCustomUserDataGradlePluginVersion" to libs.versions.commonCustomUserData)
  substitute("develocityGradlePluginVersion" to libs.versions.develocity)
  substitute("gradleDistributionSha256Sum" to gradleProperties.distributionSha256Sum)
  substitute("gradleVersion" to gradleProperties.version)
  substitute("springBootVersion" to libs.versions.springBoot)
  substitute("springCloudVersion" to libs.versions.springCloud)
  substitute("springDependencyManagementGradlePlugin" to libs.versions.springDependencyManagement)
}
