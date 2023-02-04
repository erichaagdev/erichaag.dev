import dev.erichaag.hugo.ShortcodesDirectoryArgumentProvider

plugins {
  java
  id("dev.erichaag.hugo")
}

val shortcodesDirectoryArgumentProvider = ShortcodesDirectoryArgumentProvider(layout.buildDirectory.dir("hugo/snippets/$name"))

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
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
