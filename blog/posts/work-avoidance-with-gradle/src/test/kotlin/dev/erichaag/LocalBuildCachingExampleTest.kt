package dev.erichaag

import org.junit.jupiter.api.Test
import java.time.Duration

class LocalBuildCachingExampleTest : AbstractExampleTest() {

  @Test
  fun `demonstrate incremental building`() {
    copyProject("java-example-project")

    val gradleProperties = projectDirectory.resolve("gradle.properties")
    val gradlePropertiesContent = """
      org.gradle.caching=true
    """.trimIndent()
    gradleProperties.writeText(gradlePropertiesContent)

    val gradleSettings = projectDirectory.resolve("settings.gradle.kts")
    val gradleSettingsContent = """
      buildCache {
        local {
          directory = File(rootDir, "build-cache")
        }
      }
    """.trimIndent()
    gradleSettings.appendText(gradleSettingsContent)

    // Prime the cache
    runner.withArguments("clean", "build").build()

    val firstResult = runner.withArguments("clean", "build").build()
    val secondResult = runner.run {
      val mainSourceFile = projectDirectory.resolve("src/main/java/dev/erichaag/example/App.java")
      mainSourceFile.writeText(mainSourceFile.readText().replace(Regex("return.*"), """return "Hello there!";"""))
      withArguments("clean", "build").build()
    }

    createSnippet("local-build-example-gradle-properties.md", gradlePropertiesContent)

    createSnippet("local-build-example-output-1.md", outputOf(listOf("clean", "build"), firstResult, Duration.ofMillis(100)))
    createSnippet("local-build-example-output-2.md", outputOf(listOf("clean", "build"), secondResult, Duration.ofSeconds(2)))
  }
}
