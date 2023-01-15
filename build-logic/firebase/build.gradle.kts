plugins {
  `kotlin-dsl`
}

gradlePlugin {
  plugins {
    create("firebase") {
      id = "dev.erichaag.firebase"
      implementationClass = "dev.erichaag.firebase.FirebasePlugin"
    }
  }
}
