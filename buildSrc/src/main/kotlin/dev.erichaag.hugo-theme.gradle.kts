@file:Suppress("HasPlatformType")

import dev.erichaag.hugo.theme.HugoThemeExtension
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.repositories

plugins {
  id("base")
}

val hugoThemeExtension = extensions.create<HugoThemeExtension>("hugoTheme")

val hugoThemeExports by configurations.creating {
  isCanBeConsumed = true
  isCanBeResolved = false
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
}

val loveItTheme by configurations.creating

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
  from(loveItTheme.map { tarTree(it) }) {
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

artifacts {
  add(hugoThemeExports.name, processHugoTheme)
}
