plugins {
  alias(libs.plugins.wrapperUpgrade)
}

wrapperUpgrade {
  gradle {
    register("root") {
      repo = "erichaagdev/erichaag.dev"
    }
    register("example-bootiful-builds") {
      dir = "blog/posts/bootiful-builds-best-practices-spring-boot-gradle/example"
      repo = "erichaagdev/erichaag.dev"
    }
  }
}
