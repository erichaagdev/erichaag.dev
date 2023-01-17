plugins {
  java
  id("dev.erichaag.hugo-bundle")
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}
