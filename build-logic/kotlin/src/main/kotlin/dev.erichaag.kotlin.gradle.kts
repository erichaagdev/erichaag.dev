@file:Suppress("UnstableApiUsage")

plugins {
  kotlin("jvm")
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}
