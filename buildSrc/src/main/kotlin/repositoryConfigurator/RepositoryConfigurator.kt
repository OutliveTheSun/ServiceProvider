package repositoryConfigurator

import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI

/**
 * This class adds GithubPackage repositories to gradle for consuming/publishing
 */
class RepositoryConfigurator : IRepositoryConfigurator {
    override fun addRepository(
        repositoryUrl: URI,
        repositoryUsername: String,
        repositoryPassword: String,
        repositoryHandler: RepositoryHandler
    ) {
        repositoryHandler.maven {
            url = repositoryUrl
            credentials {
                username = repositoryUsername
                password = repositoryPassword
            }
        }
    }
}