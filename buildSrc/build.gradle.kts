plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    //makes the files defined in buildSrc accessible in the modules
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
}