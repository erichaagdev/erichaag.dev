@file:Suppress("UnstableApiUsage")

import dev.erichaag.hugo.post.HugoPostExtension
import dev.erichaag.hugo.post.SnippetsDirectory

plugins {
  id("dev.erichaag.kotlin")
  id("jvm-test-suite")
}

val hugoPostExtension = extensions.create<HugoPostExtension>("hugoPost")
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
  val substitutions = hugoPostExtension.substitutions
  inputs.property("substitutions", substitutions)
  duplicatesStrategy = DuplicatesStrategy.FAIL
  into(layout.buildDirectory.dir("tasks/$name"))
  into("content/posts/${project.name}") {
    from(layout.projectDirectory.file("index.md")) {
      eachFile {
        expand(substitutions.get()) {
          escapeBackslash = true
        }
      }
    }
  }
  into("layouts/shortcodes/${project.name}") {
    from(snippetsDirectory.value)
  }
}

val hugoPostConsumable = configurations.consumable("hugoPostConsumable") {
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-post"))
  outgoing.artifact(processHugoPost)
}
