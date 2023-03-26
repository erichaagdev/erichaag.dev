plugins {
  id("dev.erichaag.kotlin")
  id("jvm-test-suite")
}

val hugoPostExports: Configuration by configurations.creating {
  isCanBeConsumed = true
  isCanBeResolved = false
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-post"))
}

val snippetsDirectory = SnippetsDirectory(layout.buildDirectory.dir("snippets/$name"))
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

val processHugoPost by tasks.registering(Sync::class) {
  dependsOn(test)
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

class SnippetsDirectory(@get:OutputDirectory val value: Provider<Directory>) : CommandLineArgumentProvider {
  override fun asArguments() = listOf("-Ddev.erichaag.post.snippets=${value.get().asFile.path}")
}
