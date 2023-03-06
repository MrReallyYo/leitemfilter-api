plugins {
    kotlin("jvm") version "1.8.0"
    id("maven-publish")
}

group = "com.github.mrreallyyo"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {

    val jackson = project.property("jackson.version")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jackson")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")


    testImplementation("org.slf4j:slf4j-simple:2.0.6")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

publishing {
    repositories {
        maven {
            name = "GitHub"
            url = uri((project.findProperty("github.uri") as String) + "leitemfilter-api")
            credentials {
                username = project.findProperty("github.user") as String
                password = project.findProperty("github.token") as String
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}