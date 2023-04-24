package dev.erichaag

import org.gradle.internal.time.TimeFormatting
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertAll
import java.io.File
import kotlin.time.Duration

class BuildResult(
  private val result: org.gradle.testkit.runner.BuildResult,
  private val requestedTasks: List<String>
) {

  private val actionableTaskCount = extractTaskCount(actionableTaskCountRegex)
  private val executedTaskCount = extractTaskCount(executedTaskCountRegex)
  private val fromCacheTaskCount = extractTaskCount(fromCacheTaskCountRegex)
  private val upToDateTaskCount = extractTaskCount(upToDateTaskCountRegex)

  fun assert(configure: AssertDsl.() -> Unit = {}) {
    val expected = AssertDsl().apply(configure)
    buildList {
      assertIfNotNull(expected.actionableTaskCount) {
        assertEquals(it, actionableTaskCount, "actionable tasks")
      }
      assertIfNotNull(expected.executedTaskCount) {
        assertEquals(it, executedTaskCount, "executed tasks")
      }
      assertIfNotNull(expected.fromCacheTaskCount) {
        assertEquals(it, fromCacheTaskCount, "from cache tasks")
      }
      assertIfNotNull(expected.totalTaskCount) {
        assertEquals(it, result.tasks.size, "total tasks")
      }
      assertIfNotNull(expected.upToDateTaskCount) {
        assertEquals(it, upToDateTaskCount, "up-to-date tasks")
      }
      assertAll("Build result assertion failures", this)
    }
  }

  fun writeSnippet(name: String, duration: Duration) {
    File(System.getProperty("dev.erichaag.snippetsDirectory"))
      .resolve("$name.md")
      .writeText(
        """
          |$ ./gradlew ${requestedTasks.joinToString(" ")}
          |${result.output.replace(Regex("BUILD SUCCESSFUL in.*"), "BUILD SUCCESSFUL in ${formatDuration(duration)}")}
        """.trimMargin()
      )
  }

  private fun extractTaskCount(regex: Regex): Int {
    val result = regex.find(result.output)
    if (result == null || result.groupValues.size != 2) {
      return 0
    }
    return result.groupValues.last().toInt()
  }

  private fun <E> MutableList<() -> Unit>.assertIfNotNull(subject: E?, assertion: (E) -> Unit) {
    if (subject != null) add { assertion(subject) }
  }

  private fun formatDuration(duration: Duration): String {
    return TimeFormatting.formatDurationTerse(duration.inWholeMilliseconds)
  }

  data class AssertDsl(
    var actionableTaskCount: Int? = null,
    var executedTaskCount: Int? = null,
    var fromCacheTaskCount: Int? = null,
    var totalTaskCount: Int? = null,
    var upToDateTaskCount: Int? = null,
  )

  private companion object {
    private val actionableTaskCountRegex = """(\d+) actionable tasks""".toRegex()
    private val executedTaskCountRegex = """(\d+) executed""".toRegex()
    private val fromCacheTaskCountRegex = """(\d+) from cache""".toRegex()
    private val upToDateTaskCountRegex = """(\d+) up-to-date""".toRegex()
  }
}
