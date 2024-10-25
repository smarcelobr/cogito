import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	java
	id("org.springframework.boot")
	id("io.spring.dependency-management")
}

group = "br.nom.figueiredo.sergio"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.mariadb:r2dbc-mariadb:1.1.3")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.flywaydb:flyway-mysql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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