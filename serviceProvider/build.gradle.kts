plugins {
    id("java-library")
    id("kotlin")
    id("maven-publish")
    signing
}

val buildProcessConfigFacade = BuildProcessConfigFacade(project)
publishing {
    buildProcessConfigFacade.setupPublishing(this@publishing, configurations.implementation)
}

//----------Publish configuration---------------
ModuleVersionNumbers.dependencyVersions[project.name] = "2.0.0"
buildProcessConfigFacade.publishSnapshotToGithubPackages()
//----------------------------------------------

//Signing is only required for publishing to Maven Central and needs to be put after the configuration of the corresponding repository (maven/GitHub)
signing {
    sign(publishing.publications["mavenJava"])
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${ProjectVersionNumbers.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${ProjectVersionNumbers.kotlin}")
    api("com.outlivethesun:reflectioninfo:${ModuleVersionNumbers.reflectionInfo}")

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("io.mockk:mockk:${ProjectVersionNumbers.mockk}")
}