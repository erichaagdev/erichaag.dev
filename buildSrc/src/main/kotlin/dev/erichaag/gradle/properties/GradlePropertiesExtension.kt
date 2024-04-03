package dev.erichaag.gradle.properties

import org.gradle.api.provider.Provider

abstract class GradlePropertiesExtension(
  val version: Provider<String>,
  val distributionSha256Sum: Provider<String>,
)
