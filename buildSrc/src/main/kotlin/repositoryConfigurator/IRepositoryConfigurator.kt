package repositoryConfigurator

import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI

/**
 * An implementation of this interface adds repositories to gradle for consuming/publishing libraries
 */
interface IRepositoryConfigurator {
    fun addRepository(
        repositoryUrl: URI,
        repositoryUsername: String,
        repositoryPassword: String,
        repositoryHandler: RepositoryHandler
    )
}