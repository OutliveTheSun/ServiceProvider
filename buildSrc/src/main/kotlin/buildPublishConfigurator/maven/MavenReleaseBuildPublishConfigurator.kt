package buildPublishConfigurator.maven

import buildPublishConfigurator.IBuildPublishConfigurator
import org.gradle.api.Project
import outlivethesunSetup.repositoryConfigurator.maven.MavenCentralReleaseRepositoryConfigurator
import pom.context.mavenContext.IPomMavenContext
import pom.context.PomContext
import libraryMetaData.ILibraryMetaData
import libraryMetaData.LibraryMetaData
import repositoryConfigurator.IPublishRepositoryConfigurator

/**
 * This class is responsible is a [IBuildPublishConfigurator].
 * It is responsible for collecting all necessary information to configure the publishing build for a release in the
 * MavenCentralRepository, so it can be composed after everything is collected.
 */
class MavenReleaseBuildPublishConfigurator(
    project: Project,
    transformPom: IPomMavenContext.() -> Unit
) : IBuildPublishConfigurator<IPomMavenContext> {
    override val publishRepositoryConfigurator: IPublishRepositoryConfigurator =
        MavenCentralReleaseRepositoryConfigurator(project)
    override val libraryMetaData: ILibraryMetaData = LibraryMetaData(project)
    override val pomContext: IPomMavenContext = PomContext()
    override val transform: IPomMavenContext.() -> Unit = transformPom
}