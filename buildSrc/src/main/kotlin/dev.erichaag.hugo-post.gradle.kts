@file:Suppress("UnstableApiUsage", "HasPlatformType")

import dev.erichaag.hugo.post.SnippetsDirectory
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.registering

plugins {
  id("jvm-test-suite")
}

val hugoPostExports by configurations.creating {
  isCanBeConsumed = true
  isCanBeResolved = false
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-post"))
}

val snippetsDirectory = SnippetsDirectory(layout.buildDirectory.dir("snippets/$name"))
val blogTest by testing.suites.creating(JvmTestSuite::class) {
  useJUnitJupiter()
  dependencies {
    implementation(gradleTestKit())
  }
  targets.all {
    testTask.configure {
      jvmArgumentProviders.add(snippetsDirectory)
    }
  }
}

val processHugoPost by tasks.registering(Sync::class) {
  dependsOn(blogTest)
  duplicatesStrategy = DuplicatesStrategy.FAIL
  into(layout.buildDirectory.dir("tasks/$name"))
  into("content/posts/${project.name}") {
    from(layout.projectDirectory.file("index.md"))
  }
  into("layouts/shortcodes/${project.name}") {
    from(snippetsDirectory.value)
  }
}

artifacts {
  add(hugoPostExports.name, processHugoPost)
}
