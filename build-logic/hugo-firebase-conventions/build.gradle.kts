plugins {
  `kotlin-dsl`
}

gradlePlugin {
  plugins {
    create("hugoFirebase") {
      id = "dev.erichaag.hugo-firebase-conventions"
      implementationClass = "dev.erichaag.hugofirebase.HugoFirebasePlugin"
    }
  }
}

dependencies {
  compileOnly(project(":firebase"))
  compileOnly(project(":hugo"))
}
