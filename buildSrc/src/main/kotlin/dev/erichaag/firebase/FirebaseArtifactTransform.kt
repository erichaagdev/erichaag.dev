package dev.erichaag.firebase

import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Provider
import org.gradle.internal.os.OperatingSystem
import java.io.File
import javax.inject.Inject

interface FirebaseArtifactTransform : TransformAction<TransformParameters.None> {

  @get:InputArtifact
  val firebaseExecutable: Provider<FileSystemLocation>

  @get:Inject
  val fileSystemOperations: FileSystemOperations

  override fun transform(outputs: TransformOutputs) {
    val os = OperatingSystem.current()
    when {
      os.isWindows -> transform(outputs.file("firebase.exe"))
      else -> transform(outputs.file("firebase"))
    }
  }

  private fun transform(outputFile: File) {
    fileSystemOperations.copy {
      from(firebaseExecutable)
      rename { outputFile.name }
      eachFile { mode = 508 }
      into(outputFile.parentFile)
    }
  }
}
