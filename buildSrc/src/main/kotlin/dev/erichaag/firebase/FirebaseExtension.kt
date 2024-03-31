package dev.erichaag.firebase

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.internal.os.OperatingSystem

abstract class FirebaseExtension(
  private val project: Project,
  private val configurationName: String,
) {

  abstract val projectName: Property<String>

  abstract val publicDirectory: DirectoryProperty

  abstract val configFile: RegularFileProperty

  fun toolchainVersion(version: Provider<String>) = with(project.dependencies) {
    val os = OperatingSystem.current()
    val osFilenamePart = when {
      os.isWindows -> "win.exe"
      os.isMacOsX -> "macos"
      os.isLinux -> "linux"
      else -> throw IllegalStateException("A Firebase binary is not available for your operating system")
    }
    add(configurationName, "firebase:firebase-tools:${version.get()}:$osFilenamePart@firebase-download")
  }
}
