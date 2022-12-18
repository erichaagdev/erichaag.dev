package dev.erichaag.common

import org.gradle.api.Action
import org.gradle.api.file.CopySpec
import org.gradle.api.file.FileSystemOperations
import javax.inject.Inject

interface CopyTask {

  @get:Inject
  val fileSystemOperations: FileSystemOperations

  fun copy(action: Action<CopySpec>) = fileSystemOperations.copy(action)
}
