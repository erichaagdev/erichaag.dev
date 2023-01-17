package dev.erichaag.hugo

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.DirectoryProperty
import java.net.URI

abstract class HugoExtension(
  private val repositories: RepositoryHandler,
) {

  abstract val publicDirectory: DirectoryProperty

  abstract val sourceDirectory: DirectoryProperty

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
