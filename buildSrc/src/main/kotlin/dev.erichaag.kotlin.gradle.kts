plugins {
  kotlin("jvm")
}

repositories {
  mavenCentral()
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}
