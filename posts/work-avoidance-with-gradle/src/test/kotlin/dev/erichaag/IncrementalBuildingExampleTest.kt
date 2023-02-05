package dev.erichaag

import org.gradle.internal.time.TimeFormatting
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.time.Duration


class IncrementalBuildingExampleTest {

  private lateinit var runner: GradleRunner

  @TempDir
  private lateinit var projectDirectory: File

  @BeforeEach
  fun beforeEach() {
    runner = GradleRunner.create().withProjectDir(projectDirectory)
  }

  @Test
  fun `demonstrate incremental building`() {
    copyProject("java-example-project")
    val firstResult = runner.withArguments("build").build()
    val secondResult = runner.withArguments("build").build()

    val mainSourceFile = projectDirectory.resolve("src/main/java/dev/erichaag/example/App.java")
    mainSourceFile.writeText(mainSourceFile.readText().replace(Regex("return.*"), """return "Hello there!";"""))
    val thirdResult = runner.withArguments("build").build()

    createSnippet("incremental-building-example-output-1.md", outputOf(listOf("build"), firstResult, Duration.ofSeconds(5)))
    createSnippet("incremental-building-example-output-2.md", outputOf(listOf("build"), secondResult, Duration.ofMillis(100)))
    createSnippet("incremental-building-example-output-3.md", outputOf(listOf("build"), thirdResult, Duration.ofSeconds(2)))
  }

  private fun createSnippet(name: String, text: String) =
    File(System.getProperty("dev.erichaag.hugo.shortcodesDirectory")).resolve(name).writeText(text)

  private fun copyProject(name: String) =
    File(javaClass.getResource("/$name")!!.path).copyRecursively(projectDirectory)

  private fun outputOf(tasks: List<String>, result: BuildResult, duration: Duration) = """
    |$ ./gradlew ${tasks.joinToString(" ")}
    |${result.output.replace(Regex("BUILD SUCCESSFUL in.*"), "BUILD SUCCESSFUL in ${formatDuration(duration)}")}
  """.trimMargin()

  private fun formatDuration(duration: Duration)
  = TimeFormatting.formatDurationTerse(duration.toMillis())
}
