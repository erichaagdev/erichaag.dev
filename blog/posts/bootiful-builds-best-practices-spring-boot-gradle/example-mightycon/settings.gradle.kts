plugins {
  id("com.gradle.develocity") version "3.17.4"
  id("com.gradle.common-custom-user-data-gradle-plugin") version "2.0.1"
}

rootProject.name = "mightycon"

include("mightycon-app")
include("mightycon-core")
include("mightycon-rest")

develocity {
  buildScan {
    uploadInBackground = !System.getenv().containsKey("CI")
    termsOfUseAgree = "yes"
    termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
  }
}
