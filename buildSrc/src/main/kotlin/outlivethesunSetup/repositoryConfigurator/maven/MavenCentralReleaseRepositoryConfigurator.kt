package outlivethesunSetup.repositoryConfigurator.maven

import org.gradle.api.Project

/**
 * This class is responsible for adding the publish repository with the MavenCentralRepository url
 */
class MavenCentralReleaseRepositoryConfigurator(project: Project) :
    MavenCentralRepositoryConfigurator(project) {
    override fun getUrl(): String {
        return "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
    }
}