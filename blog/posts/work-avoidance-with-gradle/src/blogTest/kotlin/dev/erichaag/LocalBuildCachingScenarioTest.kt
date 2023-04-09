package dev.erichaag

import org.junit.jupiter.api.Test
import java.time.Duration

class LocalBuildCachingScenarioTest : AbstractScenarioTest() {

  @Test
  fun `incremental building scenario`() {
    val gradleProperties = """
      org.gradle.caching=true
    """.trimIndent()
    project.gradleProperties.writeText(gradleProperties)

    val gradleSettings = """
      buildCache {
        local {
          directory = File(rootDir, "build-cache")
        }
      }
    """.trimIndent()
    project.gradleSettings.appendText(gradleSettings)

    // Prime the cache
    runner.withArguments("clean", "build").build()

    val firstResult = runner.withArguments("clean", "build").build()
    val secondResult = runner.run {
      project.application.replaceText(Regex("return.*"), """return "Hello there!";""")
      withArguments("clean", "build").build()
    }

    createSnippet("local-build-example-gradle-properties.md", gradleProperties)

    createSnippet("local-build-example-output-1.md", outputOf(listOf("clean", "build"), firstResult, Duration.ofMillis(100)))
    createSnippet("local-build-example-output-2.md", outputOf(listOf("clean", "build"), secondResult, Duration.ofSeconds(2)))
  }
}
