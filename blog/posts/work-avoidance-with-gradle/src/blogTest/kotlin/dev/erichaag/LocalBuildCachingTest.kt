package dev.erichaag

import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class LocalBuildCachingTest : AbstractJavaScenario() {

  @Test
  fun `local build caching`() {
    val gradleProperties = file("gradle.properties") write """
      org.gradle.caching=true
    """

    file("settings.gradle.kts") append """
      buildCache {
        local {
          directory = File(rootDir, "build-cache")
        }
      }
    """

    // Prime the cache
    build {
      tasks = listOf("clean", "build")
    }

    val result1 = build {
      tasks = listOf("clean", "build")
    }

    result1.assert {
      actionableTaskCount = 7
      executedTaskCount = 4
      fromCacheTaskCount = 3
    }

    val result2 = build {
      file("src/main/java/Greeter.java").replace("return.*", """return "Hello there!";""")
      tasks = listOf("clean", "build")
    }

    result2.assert {
      actionableTaskCount = 7
      executedTaskCount = 6
      fromCacheTaskCount = 1
    }

    result1.writeSnippet(name = "local-build-caching-output-1", duration = 100.milliseconds)
    result2.writeSnippet(name = "local-build-caching-output-2", duration = 2.seconds)

    writeSnippet(name = "local-build-caching-gradle-properties", content = gradleProperties)
  }
}
