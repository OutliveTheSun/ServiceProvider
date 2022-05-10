package buildPublishConfigurator.github

import buildPublishConfigurator.IBuildPublishConfigurator
import libraryMetaData.ILibraryMetaData
import libraryMetaData.LibraryMetaData
import libraryMetaData.GithubSnapshotLibraryMetaData
import org.gradle.api.Project
import outlivethesunSetup.repositoryConfigurator.github.GithubPackageRepositoryConfigurator
import pom.context.PomContext
import pom.context.githubPackagesContext.IPomGithubPackagesContext
import repositoryConfigurator.IPublishRepositoryConfigurator

/**
 * This class is responsible is a [IBuildPublishConfigurator].
 * It is responsible for collecting all necessary information to configure the publishing build for a release
 * on GitHubPackages, so it can be composed after everything is collected.
 */
class GithubPackageSnapshotBuildPublishConfigurator(
    project: Project,
    override val transform: (IPomGithubPackagesContext.() -> Unit)?
) : IBuildPublishConfigurator<IPomGithubPackagesContext> {
    override val publishRepositoryConfigurator: IPublishRepositoryConfigurator =
        GithubPackageRepositoryConfigurator(project)
    override val libraryMetaData: ILibraryMetaData = GithubSnapshotLibraryMetaData(LibraryMetaData(project))
    override val pomContext: IPomGithubPackagesContext = PomContext()
}