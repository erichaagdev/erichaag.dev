package dev.erichaag.hugo

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI

abstract class HugoExtension(
  private val dependencies: DependencyHandler,
  private val repositories: RepositoryHandler,
) {

  fun releasesRepository() = with(repositories) {
    exclusiveContent {
      forRepository {
        ivy {
          url = URI("https://github.com/gohugoio/hugo/releases/download")
          patternLayout { artifact("v[revision]/hugo_extended_[revision]_linux-amd64.tar.gz") }
          metadataSources { artifact() }
        }
      }
      filter { includeModule("gohugoio", "hugo") }
    }
  }

  fun toolchainVersion(version: String) = with(dependencies) {
    add("hugoArtifact", "gohugoio:hugo:$version@hugoArtifact")
  }
}
