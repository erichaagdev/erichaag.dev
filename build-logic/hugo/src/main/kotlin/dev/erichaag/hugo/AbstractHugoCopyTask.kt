package dev.erichaag.hugo

import org.gradle.api.Action
import org.gradle.api.file.CopySpec
import org.gradle.api.file.FileSystemOperations
import javax.inject.Inject

abstract class AbstractHugoCopyTask : AbstractHugoTask() {

  @get:Inject
  abstract val fileSystemOperations: FileSystemOperations

  protected fun copy(action: Action<CopySpec>) = fileSystemOperations.copy(action)
}
