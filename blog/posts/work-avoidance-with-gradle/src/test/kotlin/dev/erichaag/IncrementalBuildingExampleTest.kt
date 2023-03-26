package dev.erichaag

import org.junit.jupiter.api.Test
import java.time.Duration

class IncrementalBuildingExampleTest : AbstractExampleTest() {

  @Test
  fun `demonstrate incremental building`() {
    copyProject("java-example-project")

    val firstResult = runner.withArguments("build").build()
    val secondResult = runner.withArguments("build").build()
    val thirdResult = runner.run {
      val mainSourceFile = projectDirectory.resolve("src/main/java/dev/erichaag/example/App.java")
      mainSourceFile.writeText(mainSourceFile.readText().replace(Regex("return.*"), """return "Hello there!";"""))
      withArguments("build").build()
    }

    createSnippet("incremental-building-example-output-1.md", outputOf(listOf("build"), firstResult, Duration.ofSeconds(5)))
    createSnippet("incremental-building-example-output-2.md", outputOf(listOf("build"), secondResult, Duration.ofMillis(100)))
    createSnippet("incremental-building-example-output-3.md", outputOf(listOf("build"), thirdResult, Duration.ofSeconds(2)))
  }
}
