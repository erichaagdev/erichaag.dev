package dev.erichaag

import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
class SimpleJavaProject(projectDirectory: File) {

  val gradleProperties = projectDirectory.resolve("gradle.properties")

  val gradleSettings = projectDirectory.resolve("settings.gradle.kts")

  val gradleBuild = projectDirectory.resolve("build.gradle.kts")

  val application = projectDirectory.resolve("src/main/java/App.java")

  val applicationProperties = projectDirectory.resolve("src/main/resources/application.properties")

  val applicationTest = projectDirectory.resolve("src/test/java/App.java")

  val applicationTestProperties = projectDirectory.resolve("src/test/resources/application-test.properties")

  fun writeProject() {
    mkdirs(
      application,
      applicationProperties,
      applicationTest,
      applicationTestProperties
    )

    gradleSettings.writeText(
      """
        rootProject.name = "java"
      
      """.trimIndent()
    )

    gradleBuild.writeText(
      """
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
        
      """.trimIndent()
    )

    application.writeText(
      """
        public class App {
        public String getGreeting() {
          return "Hello world!";
        }
    
        public static void main(String[] args) {
          System.out.println(new App().getGreeting());
        }
      }
      
      """.trimIndent()
    )

    applicationProperties.writeText(
      """
        foo=bar
      
      """.trimIndent()
    )

    applicationTest.writeText(
      """
        import org.junit.jupiter.api.Assertions;
        import org.junit.jupiter.api.Test;
        
        class AppTest {
          @Test
          void appHasAGreeting() {
            App classUnderTest = new App();
            Assertions.assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
          }
        }
        
      """.trimIndent()
    )

    applicationTestProperties.writeText(
      """
        hello=world
        
      """.trimIndent()
    )
  }

  private fun mkdirs(vararg files: File) {
    files.map(File::getParentFile).forEach(File::mkdirs)
  }
}
