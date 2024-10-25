rootProject.name = "cogito"

pluginManagement {
    plugins {
//        kotlin("jvm") version "2.0.20" // Kotlin version to use
        java
        id("org.springframework.boot") version "3.3.5"
        id("io.spring.dependency-management") version "1.1.6"
    }
}

include("backend", "frontend")