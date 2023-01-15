package dev.erichaag.firebase

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import java.net.URI

abstract class FirebaseExtension(
  private val repositories: RepositoryHandler
) {

  abstract val projectName: Property<String>

  abstract val publicDirectory: DirectoryProperty

  abstract val configFile: RegularFileProperty

  fun releasesRepository() = with(repositories) {
    exclusiveContent {
      forRepository {
        ivy {
          url = URI("https://github.com/firebase/firebase-tools/releases/download")
          patternLayout { artifact("v[revision]/firebase-tools-linux") }
          metadataSources { artifact() }
        }
      }
      filter { includeModule("firebase", "firebase-tools") }
    }
  }
}
