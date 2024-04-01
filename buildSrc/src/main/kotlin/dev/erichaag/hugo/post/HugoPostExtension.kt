package dev.erichaag.hugo.post

import org.gradle.api.provider.MapProperty

abstract class HugoPostExtension {

  abstract val substitutions: MapProperty<String, String>
}
