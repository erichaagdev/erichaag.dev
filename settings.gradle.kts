plugins {
  id("com.gradle.enterprise") version "3.12"
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

rootProject.name = "erichaag.dev"
