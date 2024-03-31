@file:Suppress("UnstableApiUsage")

import dev.erichaag.hugo.theme.HugoThemeExtension
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE

plugins {
  id("base")
}

val hugoThemeDeclarable = configurations.dependencyScope("hugoTheme").get()

val hugoThemeResolvable = configurations.resolvable("${hugoThemeDeclarable.name}Resolvable") {
  extendsFrom(hugoThemeDeclarable)
}

val hugoThemeExtension = extensions.create<HugoThemeExtension>("hugoTheme", hugoThemeDeclarable.name)

repositories {
  exclusiveContent {
    forRepository {
      ivy {
        name = "LoveIt Theme"
        url = uri("https://github.com/dillonzq/LoveIt")
        patternLayout { artifact("archive/refs/tags/v[revision].[ext]") }
        metadataSources { artifact() }
      }
    }
    filter { includeModule("dillonzq", "LoveIt") }
  }
}

val processHugoTheme by tasks.registering(Sync::class) {
  duplicatesStrategy = DuplicatesStrategy.FAIL
  into(layout.buildDirectory.dir("tasks/$name"))
  from(hugoThemeResolvable.map { tarTree(it.singleFile) }) {
    exclude(
      "**/.babelrc",
      "**/.circleci/",
      "**/.github/",
      "**/.husky/",
      "**/LICENSE",
      "**/README.md",
      "**/README.zh-cn.md",
      "**/config.toml",
      "**/exampleSite/",
      "**/go.mod",
      "**/package-lock.json",
      "**/package.json",
      "**/src",
      "pax_global_header",
    )
    includeEmptyDirs = false
    eachFile {
      path = "themes/LoveIt/" + path.split("/").drop(1).joinToString("/")
    }
  }
}

val hugoThemeConsumable = configurations.consumable("${hugoThemeDeclarable.name}Consumable") {
  attributes.attribute(CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
  outgoing.artifact(processHugoTheme)
}
