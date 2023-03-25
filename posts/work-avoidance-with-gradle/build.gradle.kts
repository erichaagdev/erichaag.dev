@file:Suppress("UnstableApiUsage")

import dev.erichaag.hugo.ShortcodesDirectoryArgumentProvider

plugins {
  id("dev.erichaag.hugo")
  java
  kotlin("jvm") version "1.8.10"
}

val shortcodesDirectoryArgumentProvider = ShortcodesDirectoryArgumentProvider(layout.buildDirectory.dir("hugo/snippets/$name"))

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of("17")
  }
}

dependencies {
  testImplementation(gradleTestKit())
  testImplementation(libs.junit.jupiter)
}

tasks.test.configure {
  useJUnitPlatform()
  jvmArgumentProviders.add(shortcodesDirectoryArgumentProvider)
}

val processHugoContent by tasks.registering(Sync::class) {
  dependsOn(tasks.test)
  into(layout.buildDirectory.dir("hugo/$name"))
  into("content/posts/${project.name}") {
    from(layout.projectDirectory.file("index.md"))
  }
  into("layouts/shortcodes/${project.name}") {
    from(shortcodesDirectoryArgumentProvider.shortcodesDirectory)
  }
}

artifacts {
  add(configurations.hugoContentElements.name, processHugoContent)
}
