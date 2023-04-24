package dev.erichaag

import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class IncrementalBuildingTest : AbstractJavaScenario() {

  @Test
  fun `incremental building`() {
    val result1 = build()

    result1.assert {
      actionableTaskCount = 6
      executedTaskCount = 6
      totalTaskCount = 11
      upToDateTaskCount = 0
    }

    val result2 = build()

    result2.assert {
      actionableTaskCount = 6
      executedTaskCount = 0
      totalTaskCount = 11
      upToDateTaskCount = 6
    }

    val result3 = build {
      file("src/main/java/Greeter.java").replace("return.*", """return "Hello there!";""")
    }

    result3.assert {
      actionableTaskCount = 6
      executedTaskCount = 3
      totalTaskCount = 11
      upToDateTaskCount = 3
    }

    result1.writeSnippet(name = "incremental-building-output-1", duration = 5.seconds)
    result2.writeSnippet(name = "incremental-building-output-2", duration = 100.milliseconds)
    result3.writeSnippet(name = "incremental-building-output-3", duration = 2.seconds)
  }
}
