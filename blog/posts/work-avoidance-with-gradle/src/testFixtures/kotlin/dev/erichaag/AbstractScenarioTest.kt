package dev.erichaag

import org.gradle.internal.time.TimeFormatting
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.time.Duration

abstract class AbstractScenarioTest {

  @TempDir
  private lateinit var projectDirectory: File

  protected lateinit var runner: GradleRunner

  protected lateinit var project: SimpleJavaProject

  @BeforeEach
  fun beforeEach() {
    runner = GradleRunner.create().withProjectDir(projectDirectory)
    project = SimpleJavaProject(projectDirectory)
    project.writeProject()
  }

  protected fun createSnippet(name: String, text: String) =
    File(System.getProperty("dev.erichaag.post.snippets")).resolve(name).writeText(text)

  protected fun outputOf(tasks: List<String>, result: BuildResult, duration: Duration) = """
    |$ ./gradlew ${tasks.joinToString(" ")}
    |${result.output.replace(Regex("BUILD SUCCESSFUL in.*"), "BUILD SUCCESSFUL in ${formatDuration(duration)}")}
  """.trimMargin()

  private fun formatDuration(duration: Duration) =
    TimeFormatting.formatDurationTerse(duration.toMillis())
}
