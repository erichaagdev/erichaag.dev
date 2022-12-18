dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositories {
    mavenCentral()
  }
}

include("common")
include("firebase")
include("hugo")

rootProject.name = "build-logic"
