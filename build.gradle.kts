import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${ProjectVersionNumbers.kotlin}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        outlivethesunSetup.repositoryConfigurator.github.GithubPackageRepositoryConfigurator(rootProject)
            .addConsumeRepository(this)
    }

    //Avoids the error message "Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target 1.6"
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
repositories {
    mavenCentral()
}