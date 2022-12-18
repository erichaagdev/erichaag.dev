package dev.erichaag.firebase

import dev.erichaag.common.CopyTask
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class FirebaseInstall @Inject constructor(
  projectLayout: ProjectLayout,
) : CopyTask, AbstractFirebaseTask() {

  companion object {
    private const val FIREBASE_BINARY_NAME = "firebase"
  }

  @get:InputFiles
  abstract val fromConfiguration: Property<FileCollection>

  @get:Internal
  val intoDirectory: Provider<Directory> = projectLayout.buildDirectory.dir("bin/firebase")

  @get:OutputFile
  val binary: Provider<RegularFile> = intoDirectory.map { it.file(FIREBASE_BINARY_NAME) }

  @TaskAction
  fun action() = copy {
    from(fromConfiguration.get().singleFile)
    rename { FIREBASE_BINARY_NAME }
    eachFile { mode = 508 }
    into(intoDirectory)
  }
}
