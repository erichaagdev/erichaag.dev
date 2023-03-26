import dev.erichaag.hugo.HugoArtifactTransform
import dev.erichaag.hugo.HugoBuild
import dev.erichaag.hugo.HugoServe
import dev.erichaag.hugo.HugoVersion
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE
import org.gradle.internal.os.OperatingSystem
import java.net.URI

plugins {
  id("base")
}

val hugoExtension = extensions.create<HugoExtension>("hugo", dependencies, repositories)

val hugoExecutableConfiguration: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo-executable")
}

val hugoPost: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("hugo-post"))
}

val hugoTheme: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
}
val hugoBuild by tasks.registering(HugoBuild::class) {
  hugoExecutable = hugoExecutableConfiguration
  publicDirectory = layout.buildDirectory.dir("hugo/public")
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

val hugoVersion by tasks.registering(HugoVersion::class) {
  hugoExecutable = hugoExecutableConfiguration
}

val hugoServe by tasks.registering(HugoServe::class) {
  hugoExecutable = hugoExecutableConfiguration
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

val processHugo by tasks.registering(Sync::class) {
  duplicatesStrategy = DuplicatesStrategy.FAIL
  into(layout.buildDirectory.dir("tasks/$name"))
  into("") {
    from(layout.projectDirectory.dir("hugo"))
    from(hugoPost)
    from(hugoTheme)
  }
}

tasks.build {
  dependsOn(hugoBuild)
}

dependencies {
  registerTransform(HugoArtifactTransform::class) {
    from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo-download")
    to.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo-executable")
  }
}

abstract class HugoExtension(
  private val dependencies: DependencyHandler,
  private val repositories: RepositoryHandler
) {

  fun releasesRepository() = with(repositories) {
    exclusiveContent {
      forRepository {
        ivy {
          name = "Hugo Releases"
          url = URI("https://github.com/gohugoio/hugo")
          patternLayout { artifact("/releases/download/v[revision]/hugo_extended_[revision]_[classifier]") }
          metadataSources { artifact() }
        }
      }
      filter { includeModule("gohugoio", "hugo") }
    }
  }

  fun toolchainVersion(version: String) = with(dependencies) {
    val os = OperatingSystem.current()
    val osFilenamePart = when {
      os.isWindows -> "windows-amd64.zip"
      os.isMacOsX  -> "darwin-universal.tar.gz"
      os.isLinux && os.nativePrefix.contains("arm64") -> "linux-arm64.tar.gz"
      os.isLinux && os.nativePrefix.contains("64") -> "linux-amd64.tar.gz"
      else -> throw IllegalStateException("A Hugo binary is not available for your operating system")
    }
    add("hugoExecutableConfiguration", "gohugoio:hugo:$version:$osFilenamePart@hugo-download")
  }
}
