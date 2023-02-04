plugins {
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
  hugoTheme("dillonzq:LoveIt:0.2.11@tar.gz")
}

val processHugoTheme by tasks.registering(Sync::class) {
  into(layout.buildDirectory.dir("hugo/processHugoTheme"))
  from(configurations.hugoTheme.map { tarTree(it.singleFile) }) {
    exclude("pax_global_header")
    includeEmptyDirs = false
    eachFile {
      path = "themes/LoveIt/" + path.split("/").drop(1).joinToString("/")
    }
  }
}

artifacts {
  add(configurations.hugoThemeElements.name, processHugoTheme)
}
