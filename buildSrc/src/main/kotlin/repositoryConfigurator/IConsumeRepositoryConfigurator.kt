package repositoryConfigurator

import org.gradle.api.artifacts.dsl.RepositoryHandler

/**
 * An implementation of this interface is responsible for adding consume repositories to gradle to be used for retrieving libraries.
 * This [IConsumeRepositoryConfigurator] means repositories which need an authentication like a Github repository.
 * Even though maven repositories can be consumed there is no need to authenticate to use the repository.
 * So it is no [IConsumeRepositoryConfigurator].
 */
interface IConsumeRepositoryConfigurator {
    fun addConsumeRepository(repositoryHandler: RepositoryHandler)
}