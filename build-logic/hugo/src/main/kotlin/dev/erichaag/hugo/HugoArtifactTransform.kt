package dev.erichaag.hugo

import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Provider
import org.gradle.internal.os.OperatingSystem
import java.io.File
import javax.inject.Inject

interface HugoArtifactTransform : TransformAction<TransformParameters.None> {

  @get:InputArtifact
  val hugoArchive: Provider<FileSystemLocation>

  @get:Inject
  val archiveOperations: ArchiveOperations

  @get:Inject
  val fileSystemOperations: FileSystemOperations

  override fun transform(outputs: TransformOutputs) {
    val os = OperatingSystem.current()
    when {
      os.isWindows -> transform(outputs.file("hugo.exe"), archiveOperations::zipTree)
      else -> transform(outputs.file("hugo"), archiveOperations::tarTree)
    }
  }

  private fun transform(outputFile: File, unpack: (Any) -> FileTree) {
    fileSystemOperations.copy {
      from(unpack(archiveOperations.gzip(hugoArchive.get().asFile)))
      include(outputFile.name)
      into(outputFile.parentFile)
    }
  }
}
