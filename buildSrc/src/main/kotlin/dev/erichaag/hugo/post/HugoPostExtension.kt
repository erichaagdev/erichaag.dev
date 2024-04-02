package dev.erichaag.hugo.post

import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider

abstract class HugoPostExtension {

  abstract val substitutions: MapProperty<String, String>

  fun substitute(substitution: Pair<String, String>) {
    this.substitutions.put(substitution.first, substitution.second)
  }

  @JvmName("substituteProvider")
  fun substitute(substitution: Pair<String, Provider<String>>) {
    this.substitutions.put(substitution.first, substitution.second)
  }
}
