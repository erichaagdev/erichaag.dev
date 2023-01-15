package dev.erichaag.firebase

import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Provider
import javax.inject.Inject

interface FirebaseArtifactTransform : TransformAction<TransformParameters.None> {

  @get:InputArtifact
  val firebaseArtifact: Provider<FileSystemLocation>

  @get:Inject
  val fileSystemOperations: FileSystemOperations

  override fun transform(outputs: TransformOutputs) {
    val outputFile = outputs.file("firebase")
    fileSystemOperations.copy {
      from(firebaseArtifact)
      rename { outputFile.name }
      eachFile { mode = 508 }
      into(outputFile.parentFile)
    }
  }
}
