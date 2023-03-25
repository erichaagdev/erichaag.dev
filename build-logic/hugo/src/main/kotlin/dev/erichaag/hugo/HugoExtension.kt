package dev.erichaag.hugo

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.internal.os.OperatingSystem
import java.net.URI

abstract class HugoExtension(
  private val dependencies: DependencyHandler,
  private val repositories: RepositoryHandler,
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
    add("hugoArtifact", "gohugoio:hugo:$version:$osFilenamePart@$osArtifactType")
  }
}
