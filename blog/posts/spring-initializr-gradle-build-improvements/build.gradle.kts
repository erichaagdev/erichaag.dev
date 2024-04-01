@file:Suppress("UnstableApiUsage")

plugins {
  id("dev.erichaag.hugo-post")
}

hugoPost {
  substitutions.put("springBootVersion", libs.versions.springBoot)
}
