plugins {
  `kotlin-dsl`
}

gradlePlugin {
  plugins {
    create("hugoBundle") {
      id = "dev.erichaag.hugo-bundle"
      implementationClass = "dev.erichaag.hugo.bundle.HugoBundlePlugin"
    }
  }
}
