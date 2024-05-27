plugins {
  id("mightycon.java")
  id("org.springframework.boot")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
  implementation(platform(libs.findLibrary("spring-boot-dependencies").get()))
}

val springBootTest by testing.suites.creating(JvmTestSuite::class) {
  useJUnitJupiter()
  dependencies {
    implementation(project())
    implementation(platform(libs.findLibrary("spring-boot-dependencies").get()))
    implementation(libs.findLibrary("spring-boot-starter-test").get())
  }
  targets.all {
    testTask {
      shouldRunAfter(tasks.test)
    }
  }
}
