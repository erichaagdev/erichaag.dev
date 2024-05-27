@file:Suppress("UnstableApiUsage")

plugins {
  kotlin("jvm")
}

repositories {
  mavenCentral()
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(libs.findVersion("java").get().requiredVersion)
  }
}

pluginManager.withPlugin("jvm-test-suite") {
  tasks.check {
    dependsOn(testing.suites)
  }
}

tasks.checkKotlinGradlePluginConfigurationErrors {
  enabled = false
}
