package jar

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.kotlin.dsl.configure

/**
 * This class is responsible for configuring the build with jars, such as sourcesJar and JavadocJar
 */
class JarFilesConfigurator(private val project: Project) {
    fun configure() {
        project.configure<JavaPluginExtension> {
            // Create sources jar & javadoc jar -> or rather runs the equivalent task
            // These are needed for the dependency management system to e.g. analyze the content
            withSourcesJar()
            withJavadocJar()
        }
        //TODO: check if this is needed (it was only copied)
        project.tasks.getByName("javadoc") {
            this as Javadoc
            if (JavaVersion.current().isJava9Compatible) {
                (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
            }
        }
    }
}