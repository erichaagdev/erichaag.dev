import dev.erichaag.firebase.FirebaseArtifactTransform
import dev.erichaag.firebase.FirebaseDeploy
import dev.erichaag.firebase.FirebaseVersion
import org.gradle.internal.os.OperatingSystem
import java.net.URI

val firebaseExtension = extensions.create<FirebaseExtension>("firebase", dependencies, repositories)
firebaseExtension.configFile.convention(layout.projectDirectory.file("firebase.json"))

val firebaseExecutableConfiguration: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "firebase-executable")
}

dependencies {
  registerTransform(FirebaseArtifactTransform::class) {
    from.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "firebase-download")
    to.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "firebase-executable")
  }
}

val firebaseDeploy by tasks.registering(FirebaseDeploy::class) {
  firebaseExecutable = firebaseExecutableConfiguration
  projectName = firebaseExtension.projectName
  publicDirectory = firebaseExtension.publicDirectory
  configFile = firebaseExtension.configFile
}

val firebaseVersion by tasks.registering(FirebaseVersion::class) {
  firebaseExecutable = firebaseExecutableConfiguration
}

abstract class FirebaseExtension(
  private val dependencies: DependencyHandler,
  private val repositories: RepositoryHandler,
) {

  abstract val projectName: Property<String>

  abstract val publicDirectory: DirectoryProperty

  abstract val configFile: RegularFileProperty

  fun releasesRepository() = with(repositories) {
    exclusiveContent {
      forRepository {
        ivy {
          name = "Firebase Releases"
          url = URI("https://github.com/firebase/firebase-tools")
          patternLayout { artifact("/releases/download/v[revision]/firebase-tools-[classifier]") }
          metadataSources { artifact() }
        }
      }
      filter { includeModule("firebase", "firebase-tools") }
    }
  }

  fun toolchainVersion(version: String) = with(dependencies) {
    val os = OperatingSystem.current()
    val osFilenamePart = when {
      os.isWindows -> "win.exe"
      os.isMacOsX -> "macos"
      os.isLinux -> "linux"
      else -> throw IllegalStateException("A Firebase binary is not available for your operating system")
    }
    add("firebaseExecutableConfiguration", "firebase:firebase-tools:$version:$osFilenamePart@firebase-download")
  }
}
