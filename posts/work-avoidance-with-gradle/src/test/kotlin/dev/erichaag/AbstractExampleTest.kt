package dev.erichaag

import org.gradle.internal.time.TimeFormatting
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.time.Duration

abstract class AbstractExampleTest {

  protected lateinit var runner: GradleRunner

  @TempDir
  protected lateinit var projectDirectory: File

  @BeforeEach
  fun beforeEach() {
    runner = GradleRunner.create().withProjectDir(projectDirectory)
  }

  protected fun createSnippet(name: String, text: String) =
    File(System.getProperty("dev.erichaag.hugo.shortcodesDirectory")).resolve(name).writeText(text)

  protected fun copyProject(name: String) =
    File(javaClass.getResource("/$name")!!.path).copyRecursively(projectDirectory)

  protected fun outputOf(tasks: List<String>, result: BuildResult, duration: Duration) = """
    |$ ./gradlew ${tasks.joinToString(" ")}
    |${result.output.replace(Regex("BUILD SUCCESSFUL in.*"), "BUILD SUCCESSFUL in ${formatDuration(duration)}")}
  """.trimMargin()

  private fun formatDuration(duration: Duration) =
    TimeFormatting.formatDurationTerse(duration.toMillis())
}
