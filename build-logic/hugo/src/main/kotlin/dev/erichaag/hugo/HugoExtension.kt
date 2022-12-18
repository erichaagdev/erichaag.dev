package dev.erichaag.hugo

import org.gradle.api.provider.Property

interface HugoExtension {

  val buildDrafts: Property<Boolean>
}
