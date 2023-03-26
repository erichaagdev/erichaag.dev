import dev.erichaag.hugo.SnippetsDirectory

plugins {
  id("dev.erichaag.kotlin")
  id("jvm-test-suite")
}

val snippetsDirectory = SnippetsDirectory(layout.buildDirectory.dir("hugo/snippets/$name"))

val test by testing.suites.getting(JvmTestSuite::class) {
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
println("foo1")

val processBlogPost by tasks.registering(Sync::class) {
  dependsOn(test)
  into(layout.buildDirectory.dir("post/$name"))
  into("content/posts/${project.name}") {
    from(layout.projectDirectory.file("index.md"))
  }
  into("layouts/shortcodes/${project.name}") {
    from(snippetsDirectory.value)
  }
}

val postExports by configurations.creating {
  isCanBeConsumed = true
  isCanBeResolved = false
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("post"))
}

artifacts.add(postExports.name, processBlogPost)
