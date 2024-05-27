@file:Suppress("UnstableApiUsage")


plugins {
  id("dev.erichaag.hugo-post")
  id("dev.erichaag.gradle-properties")
}

hugoPost {
  substitute("apacheCommonsText" to libs.versions.apacheCommonsText)
  substitute("commonCustomUserDataGradlePluginVersion" to libs.versions.commonCustomUserData)
  substitute("develocityGradlePluginVersion" to libs.versions.develocity)
  substitute("foojayResolverConvention" to libs.versions.foojayResolverConvention)
  substitute("gradleDistributionSha256Sum" to gradleProperties.distributionSha256Sum)
  substitute("gradleVersion" to gradleProperties.version)
  substitute("java" to libs.versions.java)
  substitute("springBootVersion" to libs.versions.springBoot)
  substitute("springCloudVersion" to libs.versions.springCloud)
  substitute("springDependencyManagementGradlePlugin" to libs.versions.springDependencyManagement)
}

dependencies {
  blogTestImplementation(gradleTestKit())
}

val exampleBuild = layout.projectDirectory.dir("example-mightycon")

val blogTest by testing.suites.getting(JvmTestSuite::class) {
  targets.all {
    testTask {
      shouldRunAfter(verifyExample)
      jvmArgumentProviders.add(ExampleBuildDirectory(exampleBuild))
    }
  }
}

val verifyExample by tasks.registering(ExampleVerify::class) {
  exampleDirectory.set(exampleBuild)
  reportDirectory.set(layout.buildDirectory.dir(name))
}

val check by tasks.getting(DefaultTask::class) {
  dependsOn(verifyExample)
}

@DisableCachingByDefault(because = "Not worth caching")
abstract class ExampleVerify : DefaultTask() {

  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val exampleDirectory: DirectoryProperty

  @get:OutputDirectory
  abstract val reportDirectory: DirectoryProperty

  @TaskAction
  fun action() {
    val unexpectedFiles = listOf(".gradle", "build", "buildSrc/.gradle", "buildSrc/build").mapNotNull { ifExists(it) }
    if (unexpectedFiles.isNotEmpty()) {
      val message = "Unexpected file${if (unexpectedFiles.size == 1) "" else "s"}: ${unexpectedFiles.joinToString("") { "\n> $it" }}"
      reportDirectory.file("report.txt").get().asFile.writeText(message)
      throw GradleException(message)
    }
  }

  private fun ifExists(name: String) = if (exampleDirectory.dir(name).get().asFile.exists()) exampleDirectory.dir(name).get() else null
}

class ExampleBuildDirectory(
  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.RELATIVE)
  val exampleBuild: Directory
) : CommandLineArgumentProvider {
  override fun asArguments() = listOf("-Ddev.erichaag.exampleBuild=${exampleBuild.asFile.path}")
}
