enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

plugins {
  id("com.gradle.enterprise") version "3.12"
  id("com.gradle.common-custom-user-data-gradle-plugin") version "1.8.2"
}

gradleEnterprise {
  buildScan {
    isUploadInBackground = !System.getenv().containsKey("CI")
    termsOfServiceAgree = "yes"
    termsOfServiceUrl = "https://gradle.com/terms-of-service"

    publishAlways()
  }
}

includeBuild("build-logic")

include("site")
include("theme")
include("work-avoidance-with-gradle")

rootProject.name = "erichaagdev"
