import dev.erichaag.gradle.properties.GradlePropertiesExtension
import java.util.Properties

val gradleProperties = rootProject.layout.projectDirectory.file(provider { "gradle/wrapper/gradle-wrapper.properties" })

extensions.create<GradlePropertiesExtension>(
  "gradleProperties",
  provider { GradleVersion.current().version },
  gradleProperties["distributionSha256Sum"]
)

operator fun Provider<RegularFile>.get(value: String): Provider<String> {
  return map { Properties().apply { load(it.asFile.inputStream()) }.getProperty(value) }
}
