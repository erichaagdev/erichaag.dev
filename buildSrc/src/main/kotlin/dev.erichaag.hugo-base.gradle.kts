import dev.erichaag.hugo.HugoArtifactTransform

plugins {
  base
}

val hugoArtifact: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "hugo")
}

val post: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("post"))
}

val hugoTheme: Configuration by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
}

val hugoThemeElements: Configuration by configurations.creating {
  isCanBeConsumed = true
  isCanBeResolved = false
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
}

dependencies {
  registerTransform(HugoArtifactTransform::class) {
    from.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "zip")
    to.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "hugo")
  }

  registerTransform(HugoArtifactTransform::class) {
    from.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "tar.gz")
    to.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "hugo")
  }
}
