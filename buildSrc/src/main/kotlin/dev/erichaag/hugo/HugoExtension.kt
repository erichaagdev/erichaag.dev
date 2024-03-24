package dev.erichaag.hugo

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem

abstract class HugoExtension(private val project: Project) {

  fun releasesRepository() = with(project.repositories) {
    exclusiveContent {
      forRepository {
        ivy {
          name = "Hugo Releases"
          url = project.uri("https://github.com/gohugoio/hugo")
          patternLayout { artifact("/releases/download/v[revision]/hugo_extended_[revision]_[classifier]") }
          metadataSources { artifact() }
        }
      }
      filter { includeModule("gohugoio", "hugo") }
    }
  }

  fun toolchainVersion(version: String) = with(project.dependencies) {
    val os = OperatingSystem.current()
    val osFilenamePart = when {
      os.isWindows -> "windows-amd64.zip"
      os.isMacOsX -> "darwin-universal.tar.gz"
      os.isLinux && os.nativePrefix.contains("arm64") -> "linux-arm64.tar.gz"
      os.isLinux && os.nativePrefix.contains("64") -> "linux-amd64.tar.gz"
      else -> throw IllegalStateException("A Hugo binary is not available for your operating system")
    }
    add("hugoExecutableConfiguration", "gohugoio:hugo:$version:$osFilenamePart@hugo-download")
  }
}
