plugins {
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("maven-publish")
}

group = "io.mjmoore.gradle"
version = "0.0.1"
description = "Gradle plugin to manage project versioning."

repositories {
    mavenCentral()
}

dependencies {

    compileOnly(gradleApi())
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("io.mockk:mockk:1.12.4")
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins.create("versioning") {
        id = "io.mjmoore.gradle"
        implementationClass = "io.mjmoore.gradle.GradleVersioningPlugin"
    }
}

