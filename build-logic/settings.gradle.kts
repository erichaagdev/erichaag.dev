dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositories {
    mavenCentral()
  }
}

include("firebase")
include("hugo")
include("hugo-firebase-conventions")

rootProject.name = "build-logic"
