import dev.erichaag.hugo.HugoBuild

plugins {
  id("dev.erichaag.firebase")
  id("dev.erichaag.hugo")
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
  firebase("firebase:firebase-tools:11.18.0@binary")
  hugo("gohugoio:hugo:0.109.0@tar.gz")
  hugoTheme("dillonzq:LoveIt:0.2.11@tar.gz")
}

firebase {
  releasesRepository()
  projectName.set("erichaagdev")
  publicDirectory.set(tasks.named("hugoBuild", HugoBuild::class).flatMap { it.publicDirectory })
}

hugo {
  releasesRepository()
  themeName.set("LoveIt")
}
