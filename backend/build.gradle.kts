import org.springframework.boot.gradle.tasks.bundling.BootJar


plugins {
	java
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	id("org.flywaydb.flyway") version "10.20.1"
}

group = "br.nom.figueiredo.sergio"
version = "0.0.2"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

buildscript {
	apply(from="extra-properties.gradle")
	dependencies {
		// dependencia do plugin flyway
		classpath("org.flywaydb:flyway-mysql:10.20.1")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.mariadb:r2dbc-mariadb:1.1.3")
	implementation("org.flywaydb:flyway-mysql")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	implementation("org.scilab.forge:jlatexmath:1.0.7")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<Copy>("processFrontendResources") {
	// Directory containing the artifacts produced by the frontend project
	val frontendProjectDistDir = project(":frontend").layout.projectDirectory.dir("dist")
	val frontendDistDir = frontendProjectDistDir.dir("frontend/browser") //file("${frontendProjectBuildDir}/www")
	// Directory where the frontend artifacts must be copied to be packaged alltogether with the backend by the 'war'
	// plugin.
	val frontendResourcesDir = project.layout.buildDirectory.dir("resources/main/static").get()

	group = "Frontend"
	description = "Process frontend resources"

	doFirst {
		logger.info("Copiando frontend de: [{}] para [{}]...", frontendDistDir, frontendResourcesDir)
	}

	from(frontendDistDir)
	into(frontendResourcesDir)
}

tasks.named<Task>("processResources") {
	dependsOn("processFrontendResources")
}

tasks.getByName<Jar>("jar") {
	enabled = false
}

tasks.getByName<BootJar>("bootJar") {
	archiveBaseName = "cogito"
}

flyway {
	url = project.extra["FLYWAY_URL"] as String
	user = project.extra["FLYWAY_USER"] as String
	password = project.extra["FLYWAY_PASSWORD"] as String
	connectRetries = 10
	encoding = "UTF-8"


}
