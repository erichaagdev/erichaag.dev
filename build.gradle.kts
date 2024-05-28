plugins {
  alias(libs.plugins.wrapperUpgrade)
}

wrapperUpgrade {
  gradle {
    register("root") {
      repo = "erichaagdev/erichaag.dev"
    }
    register("bootiful-builds-example-mightycon") {
      dir = "blog/posts/bootiful-builds-best-practices-spring-boot-gradle/example-mightycon"
      repo = "erichaagdev/erichaag.dev"
    }
  }
}
