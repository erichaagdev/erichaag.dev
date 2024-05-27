plugins {
	id("mightycon.spring-boot")
}

dependencies {
	implementation(libs.spring.boot.starter.core)
	implementation(project(":mightycon-core"))
	implementation(project(":mightycon-rest"))
}
