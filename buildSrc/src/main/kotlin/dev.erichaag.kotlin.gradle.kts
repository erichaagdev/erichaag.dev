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
