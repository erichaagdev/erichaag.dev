plugins {
  id("base")
}

val blogTheme = extensions.create<BlogThemeExtension>("blogTheme", dependencies)

val blogThemeExports: Configuration by configurations.creating {
  isCanBeConsumed = true
  isCanBeResolved = false
  attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named("blog-theme"))
}

val loveItTheme: Configuration by configurations.creating

repositories {
  exclusiveContent {
    forRepository {
      ivy {
        name = "LoveIt Theme Repository"
        url = uri("https://github.com/dillonzq/LoveIt")
        patternLayout { artifact("archive/refs/tags/v[revision].[ext]") }
        metadataSources { artifact() }
      }
    }
    filter { includeModule("dillonzq", "LoveIt") }
  }
}

val processBlogTheme by tasks.registering(Sync::class) {
  into(layout.buildDirectory.dir("hugo/processHugoTheme"))
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
  add(blogThemeExports.name, processBlogTheme)
}

abstract class BlogThemeExtension(private val dependencies: DependencyHandler) {
  fun version(version: String) {
    dependencies.add("loveItTheme", "dillonzq:LoveIt:$version@tar.gz")
  }
}
