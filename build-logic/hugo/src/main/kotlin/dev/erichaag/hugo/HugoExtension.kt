package dev.erichaag.hugo

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import java.net.URI

abstract class HugoExtension(
  private val repositories: RepositoryHandler,
) {

  abstract val buildDrafts: Property<Boolean>

  abstract val processDirectory: DirectoryProperty

  abstract val publicDirectory: DirectoryProperty

  abstract val sourceDirectory: DirectoryProperty

  abstract val theme: Property<FileCollection>

  abstract val themeName: Property<String>

  fun releasesRepository() = with(repositories) {
    exclusiveContent {
      forRepository {
        ivy {
          url = URI("https://github.com/gohugoio/hugo/releases/download")
          patternLayout { artifact("v[revision]/hugo_extended_[revision]_linux-amd64.[ext]") }
          metadataSources { artifact() }
        }
      }
      filter { includeModule("gohugoio", "hugo") }
    }
  }
}
