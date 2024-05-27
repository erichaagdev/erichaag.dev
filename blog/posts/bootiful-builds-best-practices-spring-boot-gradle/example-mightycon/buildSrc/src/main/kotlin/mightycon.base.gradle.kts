plugins {
    id("base")
}

group = "dev.erichaag.mightycon"
version = rootProject.layout.projectDirectory.file("release/version.txt").asFile.readText().trim()
