package dev.erichaag.hugo

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.internal.os.OperatingSystem

abstract class HugoExtension(
  private val project: Project,
  private val configurationName: String,
) {

  fun toolchainVersion(version: Provider<String>) = with(project.dependencies) {
    val os = OperatingSystem.current()
    val osFilenamePart = when {
      os.isWindows -> "windows-amd64.zip"
      os.isMacOsX -> "darwin-universal.tar.gz"
      os.isLinux && os.nativePrefix.contains("arm64") -> "linux-arm64.tar.gz"
      os.isLinux && os.nativePrefix.contains("64") -> "linux-amd64.tar.gz"
      else -> throw IllegalStateException("A Hugo binary is not available for your operating system")
    }
    add(configurationName, "gohugoio:hugo:${version.get()}:$osFilenamePart@hugo-download")
  }
}
