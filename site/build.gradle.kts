import dev.erichaag.hugo.HugoBuild
import dev.erichaag.hugo.HugoProcess

plugins {
  id("dev.erichaag.hugo")
  id("dev.erichaag.firebase")
}

repositories {
  exclusiveContent {
    forRepository {
      ivy {
        url = uri("https://github.com")
        patternLayout { artifact("[organisation]/[module]/archive/refs/tags/v[revision].[ext]") }
        metadataSources { artifact() }
      }
    }
    filter { includeModule("dillonzq", "LoveIt") }
  }
}

val hugoTheme: Configuration by configurations.creating

dependencies {
  hugoTheme("dillonzq:LoveIt:0.2.11@tar.gz")
}

val buildHugo = tasks.named("buildHugo", HugoBuild::class)

firebase {
  projectName.set("erichaagdev")
  publicDirectory.set(buildHugo.flatMap { it.publicDirectory })
}

tasks.named<HugoProcess>("processHugo") {
  themeConfiguration.set(hugoTheme)
  themeName.set("LoveIt")
}
