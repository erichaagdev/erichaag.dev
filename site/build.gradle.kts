import dev.erichaag.hugo.HugoBuild

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

dependencies {
  hugoTheme("dillonzq:LoveIt:0.2.11@tar.gz")
  hugo("gohugoio:hugo:0.109.0@tar.gz")
}

val buildHugo = tasks.named("hugoBuild", HugoBuild::class)

firebase {
  projectName.set("erichaagdev")
  publicDirectory.set(buildHugo.flatMap { it.publicDirectory })
}

hugo {
  releasesRepository()
  themeName.set("LoveIt")
}
