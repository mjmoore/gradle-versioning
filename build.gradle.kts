plugins {
    id("maven-publish")
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("com.gradle.plugin-publish") version "0.18.0"
    id("io.mjmoore.gradle.gradle-versioning") version "0.0.1"
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

    // Plugin artifact must be available for integration tests
    dependsOn("publishToMavenLocal")
}

pluginBundle {
    website = "https://github.com/mjmoore/gradle-versioning"
    vcsUrl = "https://github.com/mjmoore/gradle-versioning.git"
    tags = listOf("versioning")
}

gradlePlugin {

    val pluginName = "gradle-versioning"

    plugins.create(pluginName) {
        id = "${project.group}.$pluginName"
        displayName = "Gradle Versioning"
        description = project.description
        implementationClass = "io.mjmoore.gradle.GradleVersioningPlugin"
    }
}

