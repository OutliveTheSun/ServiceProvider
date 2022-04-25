package buildPublishConfigurator.maven

import buildPublishConfigurator.IBuildPublishConfigurator
import org.gradle.api.Project
import outlivethesunSetup.pom.defaultPomMavenBuilderConfig
import outlivethesunSetup.repositoryConfigurator.maven.MavenCentralReleaseRepositoryConfigurator
import pom.context.mavenContext.IPomMavenContext
import pom.context.PomContext
import libraryMetaData.ILibraryMetaData
import libraryMetaData.LibraryMetaData
import gradlePropertiesReader.GradlePropertiesReader
import repositoryConfigurator.IPublishRepositoryConfigurator

/**
 * This class is responsible is a [IBuildPublishConfigurator].
 * It is responsible for collecting all necessary information to configure the publishing build for a release in the
 * MavenCentralRepository with our default information, so it can be composed after everything is collected.
 */
class MavenReleaseBuildPublishDefaultConfigurator(
    project: Project,
    projectDescription: String,
    libraryName: String,
    private val overrideDefaultTransform: (IPomMavenContext.() -> Unit)?
) : IBuildPublishConfigurator<IPomMavenContext> {
    override val publishRepositoryConfigurator: IPublishRepositoryConfigurator =
        MavenCentralReleaseRepositoryConfigurator(project)
    override val libraryMetaData: ILibraryMetaData = LibraryMetaData(project)
    override val pomContext: IPomMavenContext = PomContext()
    override val transform: IPomMavenContext.() -> Unit = {
        projectUrl = "${GradlePropertiesReader(project).getStringProperty("githubURLPrefix")}/$libraryName"
        this.projectDescription = projectDescription
        defaultPomMavenBuilderConfig(pomContext, project)
        overrideDefaultTransform?.let { it(this) }
    }
}