@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

include("firebase")
include("hugo")
include("hugo-post")
include("hugo-theme")
include("kotlin")

rootProject.name = "build-logic"
