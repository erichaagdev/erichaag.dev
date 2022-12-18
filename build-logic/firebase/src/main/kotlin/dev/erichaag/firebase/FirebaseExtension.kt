package dev.erichaag.firebase

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

interface FirebaseExtension {

  val projectName: Property<String>

  val publicDirectory: DirectoryProperty
}
