val hugoThemeExtension = extensions.create<HugoThemeExtension>("hugoTheme", dependencies)

val hugoThemeExports: Configuration by configurations.creating {
  isCanBeConsumed = true
  isCanBeResolved = false
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("hugo-theme"))
}

val loveItTheme: Configuration by configurations.creating

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

abstract class HugoThemeExtension(private val dependencies: DependencyHandler) {
  fun version(version: String) {
    dependencies.add("loveItTheme", "dillonzq:LoveIt:$version@tar.gz")
  }
}
