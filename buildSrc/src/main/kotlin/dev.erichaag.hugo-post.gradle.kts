@file:Suppress("UnstableApiUsage")

import dev.erichaag.hugo.post.SnippetsDirectory

plugins {
  id("dev.erichaag.kotlin")
  id("jvm-test-suite")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

val snippetsDirectory = SnippetsDirectory(layout.buildDirectory.dir("snippets/$name"))
val blogTest by testing.suites.creating(JvmTestSuite::class) {
  useJUnitJupiter(libs.findVersion("junit").get().requiredVersion)
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

val hugoPostConsumable = configurations.consumable("hugoPostConsumable") {
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-post"))
  outgoing.artifact(processHugoPost)
}
