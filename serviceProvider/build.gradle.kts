plugins {
    id("java-library")
    id("kotlin")
    id("maven-publish")
}

VersionNumbers.dependencyVersions[project.name] = "1.0.20"

val publicationManager = PublicationManager(project)
publishing {
    publicationManager.configurePublishing(
        this,
        configurations.implementation
    )
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${VersionNumbers.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${VersionNumbers.kotlin}")
    api("com.outlivethesun:reflectioninfo:1.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.junit.platform:junit-platform-suite-engine:1.8.2")
    testImplementation("io.mockk:mockk:${VersionNumbers.mockk}")
}