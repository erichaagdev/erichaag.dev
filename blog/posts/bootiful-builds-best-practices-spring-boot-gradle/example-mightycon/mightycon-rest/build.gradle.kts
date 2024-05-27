plugins {
	id("mightycon.spring-boot-library")
}

dependencies {
	api(project(":mightycon-core"))
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.web)
}
