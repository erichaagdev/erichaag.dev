plugins {
  alias(libs.plugins.wrapperUpgrade)
}

wrapperUpgrade {
  gradle {
    register("root") {
      repo = "erichaagdev/erichaag.dev"
    }
  }
}