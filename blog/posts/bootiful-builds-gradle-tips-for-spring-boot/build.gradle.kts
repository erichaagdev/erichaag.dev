plugins {
  id("dev.erichaag.hugo-post")
  id("dev.erichaag.gradle-properties")
}

hugoPost {
  substitute("commonCustomUserDataGradlePluginVersion" to libs.versions.commonCustomUserData)
  substitute("develocityGradlePluginVersion" to libs.versions.develocity)
  substitute("foojayResolverConvention" to libs.versions.foojayResolverConvention)
  substitute("gradleDistributionSha256Sum" to gradleProperties.distributionSha256Sum)
  substitute("gradleVersion" to gradleProperties.version)
  substitute("java" to libs.versions.java)
  substitute("springBootVersion" to libs.versions.springBoot)
  substitute("springCloudVersion" to libs.versions.springCloud)
  substitute("springDependencyManagementGradlePlugin" to libs.versions.springDependencyManagement)
}
