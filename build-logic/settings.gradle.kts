@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

include("firebase")
include("hugo")

rootProject.name = "build-logic"
