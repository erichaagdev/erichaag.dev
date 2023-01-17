import dev.erichaag.hugo.HugoBuild

plugins {
  id("dev.erichaag.firebase")
  id("dev.erichaag.hugo")
  id("dev.erichaag.hugo-firebase-conventions")
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
  hugoBundle(projects.workAvoidanceWithGradle)
}

val processHugoTheme by tasks.registering(Sync::class) {
  from(configurations.hugoTheme.map { tarTree(it.singleFile) }) {
    exclude("pax_global_header")
    includeEmptyDirs = false
    eachFile {
      path = path.split("/").drop(1).joinToString("/")
    }
  }
  into(layout.buildDirectory.dir("hugo/processHugoTheme"))
}

val processHugoOutputDirectory = layout.buildDirectory.dir("hugo/processHugo")
val processHugo by tasks.registering(Sync::class) {
  into(processHugoOutputDirectory)
  into("") {
    from(layout.projectDirectory.dir("hugo"))
  }
  into("content/posts") {
    from(configurations.hugoBundle)
  }
  into("themes/LoveIt") {
    from(processHugoTheme)
  }
}

tasks.withType<HugoBuild>().configureEach {
  dependsOn(processHugo)
}

firebase {
  releasesRepository()
  projectName.set("erichaagdev")
}

hugo {
  releasesRepository()
  sourceDirectory.set(processHugoOutputDirectory)
}
