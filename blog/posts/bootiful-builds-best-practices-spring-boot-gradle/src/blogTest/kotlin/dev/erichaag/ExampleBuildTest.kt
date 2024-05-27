package dev.erichaag

import java.io.File
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class ExampleBuildTest {

  @TempDir
  private lateinit var projectDirectory: File

  @Test
  fun test() {
    val exampleBuildDirectory = File(System.getProperty("dev.erichaag.exampleBuild"))
    exampleBuildDirectory.copyRecursively(projectDirectory)

    projectDirectory.resolve("settings.gradle.kts").writeText("""
      buildCache {
        local {
          directory = File(rootDir, "build-cache")
        }
      }
    """.trimIndent())

    val result = GradleRunner.create()
      .withProjectDir(projectDirectory)
      .withArguments("build")
      .forwardOutput()
      .build()

    println(result.output)
  }

}
