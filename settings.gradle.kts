enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
  id("com.gradle.enterprise") version "3.16.2"
  id("com.gradle.common-custom-user-data-gradle-plugin") version "1.13"
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
include(":blog:posts:spring-initializr-gradle-build-improvements")
include(":blog:posts:work-avoidance-with-gradle")

rootProject.name = "erichaagdev"
