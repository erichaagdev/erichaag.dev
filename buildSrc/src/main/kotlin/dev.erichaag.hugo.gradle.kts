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

val hugo = extensions.create<HugoExtension>("hugo", dependencies, repositories)

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

val hugoExecutable: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo")
}

val hugoBuild by tasks.registering(HugoBuild::class) {
  hugo = hugoExecutable
  publicDirectory = layout.buildDirectory.dir("hugo/public")
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

val hugoVersion by tasks.registering(HugoVersion::class) {
  hugo = hugoExecutable
}

val hugoServe by tasks.registering(HugoServe::class) {
  hugo = hugoExecutable
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
    from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "zip")
    to.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo")
  }

  registerTransform(HugoArtifactTransform::class) {
    from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "tar.gz")
    to.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo")
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
          patternLayout { artifact("/releases/download/v[revision]/hugo_extended_[revision]_[classifier].[ext]") }
          metadataSources { artifact() }
        }
      }
      filter { includeModule("gohugoio", "hugo") }
    }
  }

  fun toolchainVersion(version: String) = with(dependencies) {
    val os = OperatingSystem.current()
    val (osFilenamePart, osArtifactType) = when {
      os.isWindows -> "windows-amd64" to "zip"
      os.isMacOsX  -> "darwin-universal" to "tar.gz"
      os.isLinux && os.nativePrefix.contains("arm64") -> "linux-arm64" to "tar.gz"
      os.isLinux && os.nativePrefix.contains("64") -> "linux-amd64" to "tar.gz"
      else -> throw IllegalStateException("There are no Hugo binaries available for your OS")
    }
    add("hugoExecutable", "gohugoio:hugo:$version:$osFilenamePart@$osArtifactType")
  }
}
