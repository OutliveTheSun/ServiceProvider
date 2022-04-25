plugins {
    id("java-library")
    id("kotlin")
    id("maven-publish")
    signing
}

VersionNumbers.dependencyVersions[project.name] = "1.0.21"

val buildProcessConfigFacade = BuildProcessConfigFacade(project)
publishing {
    buildProcessConfigFacade.setupPublishing(this@publishing, configurations.implementation)
}

/**
 * Configure here where you would like to publish to
 */
//TODO
//buildProcessConfigFacade.publishToGithubPackages()
//buildProcessConfigFacade.publishSnapshotToMavenCentralWithDefaultValues("Test")
//buildProcessConfigFacade.publishToMavenCentralWithDefaultValues()

//Signing is only required for publishing to Maven Central and needs to be put after the configuration of the corresponding repository (maven/GitHub)
//signing {
//    sign(publishing.publications["mavenJava"])
//}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${VersionNumbers.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${VersionNumbers.kotlin}")
    api("com.outlivethesun:reflectioninfo:1.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.junit.platform:junit-platform-suite-engine:1.8.2")
    testImplementation("io.mockk:mockk:${VersionNumbers.mockk}")
}