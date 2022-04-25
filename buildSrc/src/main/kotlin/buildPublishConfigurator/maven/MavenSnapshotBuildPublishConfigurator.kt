package buildPublishConfigurator.maven

import buildPublishConfigurator.IBuildPublishConfigurator
import org.gradle.api.Project
import outlivethesunSetup.repositoryConfigurator.maven.MavenCentralSnapshotRepositoryConfigurator
import pom.context.PomContext
import pom.context.mavenContext.IPomMavenContext
import libraryMetaData.ILibraryMetaData
import libraryMetaData.LibraryMetaData
import libraryMetaData.LibrarySnapshotMetaData
import repositoryConfigurator.IPublishRepositoryConfigurator

/**
 * This class is responsible is a [IBuildPublishConfigurator].
 * It is responsible for collecting all necessary information to configure the publishing build for a test=snapshot release
 * in the MavenCentralRepository, so it can be composed after everything is collected.
 */
class MavenSnapshotBuildPublishConfigurator(
    project: Project,
    private val transformPom: IPomMavenContext.() -> Unit
) : IBuildPublishConfigurator<IPomMavenContext> {
    override val publishRepositoryConfigurator: IPublishRepositoryConfigurator =
        MavenCentralSnapshotRepositoryConfigurator(project)
    override val libraryMetaData: ILibraryMetaData = LibrarySnapshotMetaData(LibraryMetaData(project))
    override val pomContext: IPomMavenContext = PomContext()
    override val transform: IPomMavenContext.() -> Unit = transformPom
}