package dev.erichaag.firebase

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject
import org.gradle.api.tasks.UntrackedTask
import org.gradle.process.ExecOperations

@UntrackedTask(because = "Not worth tracking")
abstract class FirebaseDeploy @Inject constructor(
  execOperations: ExecOperations,
  private val fileSystemOperations: FileSystemOperations,
) : AbstractFirebaseTask(execOperations) {

  @get:Input
  abstract val projectName: Property<String>

  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val publicDirectory: DirectoryProperty

  @get:InputFile
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val configFile: RegularFileProperty

  @TaskAction
  fun action() {
    fileSystemOperations.copy {
      from(configFile)
      from(publicDirectory) {
        into("public")
      }
      into(temporaryDir)
    }
    firebaseExec {
      args("deploy")
      args("--project", projectName.get())
      args("--public", "public")
      args("--config", temporaryDir.resolve("firebase.json").path)
    }
  }
}
