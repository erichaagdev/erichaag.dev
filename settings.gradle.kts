enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
  id("com.gradle.develocity") version "4.0.1"
  id("com.gradle.common-custom-user-data-gradle-plugin") version "2.2.1"
}

develocity {
  buildScan {
    uploadInBackground = !System.getenv().containsKey("CI")
    termsOfUseAgree = "yes"
    termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
  }
}

include(":blog:site")
include(":blog:posts:bootiful-builds-best-practices-spring-boot-gradle")
include(":blog:posts:work-avoidance-with-gradle")

rootProject.name = "erichaagdev"
