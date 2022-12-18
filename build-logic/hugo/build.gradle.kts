plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(project(":common"))
}

gradlePlugin {
  plugins {
    create("hugo") {
      id = "dev.erichaag.hugo"
      implementationClass = "dev.erichaag.hugo.HugoPlugin"
    }
  }
}
