package dev.erichaag.firebase

import dev.erichaag.common.ExecTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class FirebaseDeploy : ExecTask, AbstractFirebaseTask() {

  @get:Input
  abstract val projectName: Property<String>

  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val publicDirectory: DirectoryProperty

  @TaskAction
  fun action() {
    val config = File(temporaryDir, "firebase.json")
    config.writeText("""{"hosting":{}}""")
    binaryExec {
      args("deploy")
      args("--project", projectName.get())
      args("--public", publicDirectory.path)
      args("--config", config.path)
    }
  }
}
