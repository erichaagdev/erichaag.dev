plugins {
  `kotlin-dsl`
}

gradlePlugin {
  plugins {
    create("hugo") {
      id = "dev.erichaag.hugo"
      implementationClass = "dev.erichaag.hugo.HugoPlugin"
    }
  }
}
