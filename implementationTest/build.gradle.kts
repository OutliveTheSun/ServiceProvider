plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${ProjectVersionNumbers.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${ProjectVersionNumbers.kotlin}")
    implementation(project(":serviceProvider"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
}