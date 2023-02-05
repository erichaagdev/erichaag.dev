plugins {
  application
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

application {
  mainClass.set("dev.erichaag.example.App")
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}
