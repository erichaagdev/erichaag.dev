package dev.erichaag

import org.junit.jupiter.api.BeforeEach

abstract class AbstractJavaScenario : AbstractGradleScenario() {

  @BeforeEach
  override fun beforeEach() {
    super.beforeEach()

    file("settings.gradle.kts") write """
      rootProject.name = "sample-project"
    """

    file("build.gradle.kts") write """
      plugins {
        java
      }
      repositories {
        mavenCentral()
      }
      dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
      }
      tasks.test.configure {
        useJUnitPlatform()
      }
    """

    file("src/main/java/Greeter.java") write """
      public class Greeter {
        public String getGreeting() {
          return "Hello world!";
        }
      }
    """

    file("src/main/resources/application.properties") write """
      foo=bar
    """

    file("src/test/java/GreeterTest.java") write """
      import org.junit.jupiter.api.Assertions;
      import org.junit.jupiter.api.Test;
      class ApplicationTest {
        @Test
        void greeterGreets() {
          Assertions.assertNotNull(new Greeter().getGreeting());
        }
      }
    """

    file("src/test/resources/application-test.properties") write """
      hello=world
    """
  }
}
