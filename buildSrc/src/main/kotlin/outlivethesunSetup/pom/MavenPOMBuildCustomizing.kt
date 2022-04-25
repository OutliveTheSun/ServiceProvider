package outlivethesunSetup.pom

import org.gradle.api.Project
import pom.context.mavenContext.IPomMavenContext
import pom.model.SourceCodeManagement
import gradlePropertiesReader.GradlePropertiesReader

/**
 * This function contains our current default setup for publishing to the Maven Central Repository
 */
val defaultPomMavenBuilderConfig: IPomMavenContext.(project: Project) -> Unit = { project ->
    val githubUrlPrefix = GradlePropertiesReader(project).getStringProperty("githubURLPrefix")
    hasMITLicense = true
    addDeveloper("Yobuligo", "outlivethesun@gmail.com", "Outlivethesun", "http://www.outlivethesun.com")
    addDeveloper("acoria", "outlivethesun@gmail.com", "Outlivethesun", "http://www.outlivethesun.com")
    hasDistributionManagement = true
    hasNexusStagingPlugin = true
    hasSources = true
    hasJavaDoc = true
    val githubUrl = "$githubUrlPrefix}/${project.rootProject.name}.git"
    sourceCodeManagement =
        SourceCodeManagement("scm:git:git://$githubUrl", "scm:git:ssh://$githubUrl", githubUrlPrefix)
}