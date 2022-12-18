plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(project(":common"))
}

gradlePlugin {
  plugins {
    create("firebase") {
      id = "dev.erichaag.firebase"
      implementationClass = "dev.erichaag.firebase.FirebasePlugin"
    }
  }
}
