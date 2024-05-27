plugins {
  id("java")
  id("jvm-test-suite")
  id("mightycon.base")
}

repositories {
  mavenCentral()
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
    vendor = JvmVendorSpec.ADOPTIUM
  }
}

val test by testing.suites.getting(JvmTestSuite::class) {
  useJUnitJupiter()
}

tasks.check {
  dependsOn(testing.suites)
}
