package dev.erichaag.hugo

import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Provider
import javax.inject.Inject

interface HugoArtifactTransform : TransformAction<TransformParameters.None> {

  @get:InputArtifact
  val hugoArtifact: Provider<FileSystemLocation>

  @get:Inject
  val archiveOperations: ArchiveOperations

  @get:Inject
  val fileSystemOperations: FileSystemOperations

  override fun transform(outputs: TransformOutputs) {
    val outputFile = outputs.file("hugo")
    fileSystemOperations.copy {
      from(archiveOperations.tarTree(hugoArtifact.get().asFile))
      include(outputFile.name)
      into(outputFile.parentFile)
    }
  }
}
