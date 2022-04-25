import buildPublishConfigurator.github.GithubPackageBuildPublishConfigurator
import buildPublishConfigurator.maven.MavenReleaseBuildPublishConfigurator
import buildPublishConfigurator.maven.MavenReleaseBuildPublishDefaultConfigurator
import buildPublishConfigurator.maven.MavenSnapshotBuildPublishConfigurator
import buildPublishConfigurator.maven.MavenSnapshotBuildPublishDefaultConfigurator
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import pom.context.githubPackagesContext.IPomGithubPackagesContext
import pom.context.mavenContext.IPomMavenContext
import pom.context.IPomContext
import buildProcessComposer.IBuildProcessComposer
import buildProcessComposer.BuildProcessComposer
import buildProcessConfigFacade.IBuildProcessConfigFacade
import buildProcessConfigFacade.PublishingNotSetUpException
import buildPublishConfigurator.IBuildPublishConfigurator

/**
 * This class is responsible for handling the configuration of the build.gradle file (e.g. for adding library publishing)
 */
class BuildProcessConfigFacade(override val project: Project) : IBuildProcessConfigFacade {
    private var needsSetupForPublishing: Boolean = true
    private lateinit var publishingExtension: PublishingExtension
    private lateinit var implementations: NamedDomainObjectProvider<Configuration>

    init {
        setKotlinCompileVersion()
        configureJavaContainer()
    }

    override fun publishToGithubPackages(transform: (IPomGithubPackagesContext.() -> Unit)?) {
        compose(GithubPackageBuildPublishConfigurator(project, transform))
    }

    override fun publishToMavenCentral(transform: IPomMavenContext.() -> Unit) {
        compose(MavenReleaseBuildPublishConfigurator(project, transform))
    }

    override fun publishSnapshotToMavenCentral(transform: IPomMavenContext.() -> Unit) {
        compose(MavenSnapshotBuildPublishConfigurator(project, transform))
    }

    override fun publishToMavenCentralWithDefaultValues(
        projectDescription: String,
        libraryName: String,
        overrideDefaultTransform: (IPomMavenContext.() -> Unit)?
    ) {
        compose(
            MavenReleaseBuildPublishDefaultConfigurator(
                project,
                projectDescription,
                libraryName,
                overrideDefaultTransform
            )
        )
    }

    override fun publishSnapshotToMavenCentralWithDefaultValues(
        projectDescription: String,
        libraryName: String,
        overrideDefaultTransform: (IPomMavenContext.() -> Unit)?
    ) {
        compose(
            MavenSnapshotBuildPublishDefaultConfigurator(
                project,
                projectDescription,
                libraryName,
                overrideDefaultTransform
            )
        )
    }

    private fun <T : IPomContext> compose(buildPublishConfigurator: IBuildPublishConfigurator<T>) {
        if (needsSetupForPublishing) {
            throw PublishingNotSetUpException()
        }
        BuildProcessComposer(project, publishingExtension, implementations, buildPublishConfigurator).compose()
    }

    override fun setupPublishing(
        publishingExtension: PublishingExtension,
        implementations: NamedDomainObjectProvider<Configuration>
    ) {
        this.needsSetupForPublishing = false
        this.publishingExtension = publishingExtension
        this.implementations = implementations
    }

    private fun setKotlinCompileVersion() {
        // Ensures that lambdas with functional interfaces can be used
        val compileKotlin: KotlinCompile by project.tasks
        compileKotlin.kotlinOptions {
            languageVersion = "1.4"
        }
    }

    private fun configureJavaContainer() {
        project.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}