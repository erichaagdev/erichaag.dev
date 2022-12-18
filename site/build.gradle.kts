import dev.erichaag.hugo.HugoProcess

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

val hugoTheme: Configuration by configurations.creating

dependencies {
  hugoTheme("dillonzq:LoveIt:0.2.11@tar.gz")
}

tasks.named<HugoProcess>("processHugo") {
  theme.set(hugoTheme)
  themeName.set("LoveIt")
}
