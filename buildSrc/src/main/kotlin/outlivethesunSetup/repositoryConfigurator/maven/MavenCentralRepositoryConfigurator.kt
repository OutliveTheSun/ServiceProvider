package outlivethesunSetup.repositoryConfigurator.maven

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import gradlePropertiesReader.GradlePropertiesReader
import repositoryConfigurator.IPublishRepositoryConfigurator
import repositoryConfigurator.IRepositoryConfigurator
import repositoryConfigurator.RepositoryConfigurator

/**
 * This class is responsible for adding the repository information such as url and authentication information for the MavenCentralRepository
 */
abstract class MavenCentralRepositoryConfigurator(private val project: Project) :
    IPublishRepositoryConfigurator {
    private val repositoryConfigurator: IRepositoryConfigurator by lazy { RepositoryConfigurator() }

    override fun addPublishRepository(repositoryHandler: RepositoryHandler) {
        val gradlePropertiesReader = GradlePropertiesReader(project)
        repositoryConfigurator.addRepository(
            project.uri(getUrl()),
            gradlePropertiesReader.getStringProperty("sonatypeNexusUsername"),
            gradlePropertiesReader.getStringProperty("sonatypeNexusPassword"),
            repositoryHandler
        )
    }

    abstract fun getUrl(): String
}