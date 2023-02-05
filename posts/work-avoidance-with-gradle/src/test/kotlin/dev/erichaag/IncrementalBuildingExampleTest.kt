package dev.erichaag

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class IncrementalBuildingExampleTest {

  @Test
  fun testIncrementalBuilding(@TempDir projectDirectory: File) {
    val shortcodesDirectory = System.getProperty("dev.erichaag.hugo.shortcodesDirectory")
    val exampleProject = File(javaClass.getResource("/java-example-project")!!.path)
    exampleProject.copyRecursively(projectDirectory)

    GradleRunner.create()
      .withProjectDir(projectDirectory)
      .withArguments("clean", "build")
      .build()

    val result = GradleRunner.create()
      .withProjectDir(projectDirectory)
      .withArguments("build")
      .build()

    val output = result.output.replace(Regex("BUILD SUCCESSFUL in.*"), "BUILD SUCCESSFUL in 10ms")

    File(shortcodesDirectory).resolve("incremental-building-example-output.md").writeText(output)
  }
}
