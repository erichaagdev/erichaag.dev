import dev.erichaag.hugo.HugoArtifactTransform
import dev.erichaag.hugo.HugoBuild
import dev.erichaag.hugo.HugoExtension
import dev.erichaag.hugo.HugoServe
import dev.erichaag.hugo.HugoVersion
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE

plugins {
  id("base")
}

val hugo = extensions.create<HugoExtension>("hugo")

val blogPost: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("blog-post"))
}

val blogTheme: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("blog-theme"))
}

val hugoExecutable: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo")
}

val hugoBuild by tasks.registering(HugoBuild::class) {
  hugo = hugoExecutable
  publicDirectory = layout.buildDirectory.dir("hugo/public")
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

val hugoVersion by tasks.registering(HugoVersion::class) {
  hugo = hugoExecutable
}

val hugoServe by tasks.registering(HugoServe::class) {
  hugo = hugoExecutable
  sourceDirectory.fileProvider(processHugo.map { it.destinationDir })
}

val processHugo by tasks.registering(Sync::class) {
  duplicatesStrategy = DuplicatesStrategy.FAIL

  into(layout.buildDirectory.dir("hugo/processHugo"))
  into("") {
    from(layout.projectDirectory.dir("hugo"))
    from(blogPost)
    from(blogTheme)
  }
}

tasks.build.configure {
  dependsOn(hugoBuild)
}

dependencies {
  registerTransform(HugoArtifactTransform::class) {
    from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "zip")
    to.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo")
  }

  registerTransform(HugoArtifactTransform::class) {
    from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "tar.gz")
    to.attribute(ARTIFACT_TYPE_ATTRIBUTE, "hugo")
  }
}
