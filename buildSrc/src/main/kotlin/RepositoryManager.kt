import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler

class RepositoryManager(private val project: Project) {

    private val githubPackagesName = "GitHubPackages"

    fun addPublicationRepository(repositoryHandler: RepositoryHandler) {
        repositoryHandler.maven {
            name = githubPackagesName
            url = project.uri("${Config.githubURL}/${project.rootProject.name}")
            credentials {
                username = Config.githubLibraryUser
                password = Config.githubLibraryKeyPublish
            }
        }
    }

    fun addConsumeRepository(repositoryHandler: RepositoryHandler) {
        repositoryHandler.maven {
            name = githubPackagesName
            url = project.uri("${Config.githubURL}/*")
            credentials {
                username = Config.githubLibraryUser
                password = Config.githubLibraryKeyConsume
            }
        }
    }
}