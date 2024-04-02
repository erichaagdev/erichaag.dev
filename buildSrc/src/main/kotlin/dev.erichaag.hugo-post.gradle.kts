@file:Suppress("UnstableApiUsage")

import dev.erichaag.hugo.post.HugoPostExtension
import dev.erichaag.hugo.post.ProcessHugoPost
import dev.erichaag.hugo.post.SnippetsDirectory

plugins {
  id("dev.erichaag.kotlin")
  id("jvm-test-suite")
}

val hugoPostExtension = extensions.create<HugoPostExtension>("hugoPost")
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

val snippetsDirectoryArgumentProvider = SnippetsDirectory(layout.buildDirectory.dir("snippets/$name"))
val blogTest by testing.suites.creating(JvmTestSuite::class) {
  useJUnitJupiter(libs.findVersion("junit").get().requiredVersion)
  targets.all {
    testTask.configure {
      jvmArgumentProviders.add(snippetsDirectoryArgumentProvider)
    }
  }
}

val processHugoPost by tasks.registering(ProcessHugoPost::class) {
  dependsOn(blogTest)
  index = layout.projectDirectory.file("index.md")
  contentPath = provider { project.name }
  substitutions = hugoPostExtension.substitutions
  snippetsDirectory = snippetsDirectoryArgumentProvider.value
  outputDirectory = layout.buildDirectory.dir("tasks/$name")
}

val hugoPostConsumable = configurations.consumable("hugoPostConsumable") {
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-post"))
  outgoing.artifact(processHugoPost)
}
