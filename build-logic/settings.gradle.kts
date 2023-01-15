dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositories {
    mavenCentral()
  }
}

include("firebase")
include("hugo")

rootProject.name = "build-logic"
