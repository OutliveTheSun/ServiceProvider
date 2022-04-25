package repositoryConfigurator

import org.gradle.api.artifacts.dsl.RepositoryHandler

/**
 * An implementation of this interface is responsible for adding publish repositories to gradle to be used for publishing libraries
 */
interface IPublishRepositoryConfigurator {
    fun addPublishRepository(repositoryHandler: RepositoryHandler)
}