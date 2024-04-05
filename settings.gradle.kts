enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
  id("com.gradle.enterprise") version "3.17"
  id("com.gradle.common-custom-user-data-gradle-plugin") version "2"
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
include(":blog:posts:bootiful-builds-gradle-tips-for-spring-boot")
include(":blog:posts:work-avoidance-with-gradle")

rootProject.name = "erichaagdev"
