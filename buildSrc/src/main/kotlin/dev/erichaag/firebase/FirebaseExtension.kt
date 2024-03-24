package dev.erichaag.firebase

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.internal.os.OperatingSystem

abstract class FirebaseExtension(private val project: Project) {

  abstract val projectName: Property<String>

  abstract val publicDirectory: DirectoryProperty

  abstract val configFile: RegularFileProperty

  fun releasesRepository() = with(project.repositories) {
    exclusiveContent {
      forRepository {
        ivy {
          name = "Firebase Releases"
          url = project.uri("https://github.com/firebase/firebase-tools")
          patternLayout { artifact("/releases/download/v[revision]/firebase-tools-[classifier]") }
          metadataSources { artifact() }
        }
      }
      filter { includeModule("firebase", "firebase-tools") }
    }
  }

  fun toolchainVersion(version: String) = with(project.dependencies) {
    val os = OperatingSystem.current()
    val osFilenamePart = when {
      os.isWindows -> "win.exe"
      os.isMacOsX -> "macos"
      os.isLinux -> "linux"
      else -> throw IllegalStateException("A Firebase binary is not available for your operating system")
    }
    add("firebaseExecutableConfiguration", "firebase:firebase-tools:$version:$osFilenamePart@firebase-download")
  }
}
