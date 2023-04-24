package dev.erichaag

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.io.File

abstract class AbstractGradleScenario {

  @TempDir
  private lateinit var projectDirectory: File

  private lateinit var gradleRunner: GradleRunner

  @BeforeEach
  open fun beforeEach() {
    gradleRunner = GradleRunner.create().withProjectDir(projectDirectory)
  }

  fun file(name: String) = ProjectFile(projectDirectory.resolve(name))

  fun build(configure: BuildDsl.() -> Unit = {}): BuildResult {
    val dsl = BuildDsl().apply(configure)
    val result = gradleRunner
      .withArguments(dsl.tasks)
      .forwardOutput()
      .build()
    return BuildResult(result, dsl.tasks)
  }

  fun writeSnippet(name: String, content: String) {
    File(System.getProperty("dev.erichaag.snippetsDirectory")).resolve("$name.md").writeText(content)
  }

  data class BuildDsl(
    var tasks: List<String> = listOf("build"),
  )
}
