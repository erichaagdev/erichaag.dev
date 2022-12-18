package dev.erichaag.hugo

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

interface HugoExtension {

  val buildDrafts: Property<Boolean>

  val processDirectory: DirectoryProperty

  val publicDirectory: DirectoryProperty

  val sourceDirectory: DirectoryProperty
}
