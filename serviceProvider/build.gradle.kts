plugins {
    id("java-library")
    id("kotlin")
    id("maven-publish")
}

VersionNumbers.dependencyVersions[project.name] = "1.0.13"

val publicationManager = PublicationManager(project)
publishing {
    publicationManager.configurePublishing(
        this,
        configurations.implementation
    )
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${VersionNumbers.kotlin}")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.30-RC")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("io.mockk:mockk:${VersionNumbers.mockk}")
}