plugins {
  id("dev.erichaag.hugo-post")
  id("dev.erichaag.kotlin")
  id("java-test-fixtures")
}

dependencies {
  blogTestImplementation(testFixtures(projects.blog.posts.workAvoidanceWithGradle))
  testFixturesImplementation(gradleTestKit())
  testFixturesImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}
