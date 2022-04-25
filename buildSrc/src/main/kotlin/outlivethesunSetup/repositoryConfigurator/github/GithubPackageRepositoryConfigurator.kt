package outlivethesunSetup.repositoryConfigurator.github

import gradlePropertiesReader.GradlePropertiesReader
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import repositoryConfigurator.IConsumeRepositoryConfigurator
import repositoryConfigurator.IPublishRepositoryConfigurator
import repositoryConfigurator.IRepositoryConfigurator
import repositoryConfigurator.RepositoryConfigurator
import java.net.URI

/**
 * This class is responsible for adding consume/publish repositories with our default GitHubPackage repository url
 */
class GithubPackageRepositoryConfigurator(private val project: Project) :
    IPublishRepositoryConfigurator, IConsumeRepositoryConfigurator {
    private val repositoryConfigurator: IRepositoryConfigurator =
        RepositoryConfigurator()
    private val gradlePropertiesReader by lazy { GradlePropertiesReader(project) }

    override fun addConsumeRepository(repositoryHandler: RepositoryHandler) {
        addRepository(
            repositoryHandler,
            project.uri("${gradlePropertiesReader.getStringProperty("githubPackageURL")}/*")
        )
    }

    override fun addPublishRepository(repositoryHandler: RepositoryHandler) {
        addRepository(
            repositoryHandler,
            project.uri("${gradlePropertiesReader.getStringProperty("githubPackageURL")}/${project.rootProject.name}")
        )
    }

    private fun addRepository(repositoryHandler: RepositoryHandler, url: URI) {
        repositoryConfigurator.addRepository(
            url,
            gradlePropertiesReader.getStringProperty("githubLibraryUser"),
            gradlePropertiesReader.getStringProperty("githubLibraryKeyPublish"),
            repositoryHandler
        )
    }
}