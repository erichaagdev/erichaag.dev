@file:Suppress("UnstableApiUsage")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
  id("com.gradle.enterprise") version "3.12.6"
  id("com.gradle.common-custom-user-data-gradle-plugin") version "1.10"
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

gradleEnterprise {
  buildScan {
    publishAlways()
    isUploadInBackground = !System.getenv().containsKey("CI")
    termsOfServiceAgree = "yes"
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
  }
}

include(":blog:site")
include(":blog:theme")
include(":blog:posts:work-avoidance-with-gradle")

rootProject.name = "erichaagdev"
