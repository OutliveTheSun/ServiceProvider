package buildProcessConfigFacade

import ModuleInfo
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.publish.PublishingExtension
import pom.context.githubPackagesContext.IPomGithubPackagesContext
import pom.context.mavenContext.IPomMavenContext

/**
 * An implementation of this interface is responsible for providing all functionality for the build process.
 */
interface IBuildProcessConfigFacade {
    val project: Project

    /**
     * This sets the project up to be published in the GithubPackages Repository.
     */
    fun publishToGithubPackages(transform: (IPomGithubPackagesContext.() -> Unit)? = null)

    /**
     * This sets the project up to be published as a snapshot(test version) in the GithubPackages Repository.
     * The version number will be marked with "-SNAPSHOT" e.g. "1.2.0-SNAPSHOT"
     */
    fun publishSnapshotToGithubPackages(transform: (IPomGithubPackagesContext.() -> Unit)? = null)

    /**
     * This sets the project up to be published in the Maven Central Repository.
     * All attributes from the [IPomMavenContext] have to be supplied.
     */
    fun publishToMavenCentral(
        transform: IPomMavenContext.() -> Unit
    )

    /**
     * This sets the project up to be published in the Maven Central Snapshot Repository.
     * The Snapshot Repository is used for test purposes.
     * All attributes from the [IPomMavenContext] have to be supplied.
     */
    fun publishSnapshotToMavenCentral(
        transform: IPomMavenContext.() -> Unit
    )

    /**
     * This sets the project up to be published in the Maven Central Repository with our default values.
     * A [projectDescription] has to be provided for this to be included in the pom.mxl.
     * The [libraryName] is usually the artifactId (= the name of the library module) but can be overridden.
     */
    fun publishToMavenCentralWithDefaultValues(
        projectDescription: String,
        libraryName: String = ModuleInfo(project).getArtifactId(),
        overrideDefaultTransform: (IPomMavenContext.() -> Unit)? = null
    )

    /**
     * This sets the project up to be published in the Maven Central Snapshot Repository with our default values.
     * The Snapshot Repository is used for test purposes.
     * A [projectDescription] has to be provided for this to be included in the pom.mxl.
     * The [libraryName] is usually the artifactId (= the name of the library module) but can be overridden.
     */
    fun publishSnapshotToMavenCentralWithDefaultValues(
        projectDescription: String,
        libraryName: String = ModuleInfo(project).getArtifactId(),
        overrideDefaultTransform: (IPomMavenContext.() -> Unit)? = null
    )

    /**
     * This sets up the project for publishing
     */
    fun setupPublishing(
        publishingExtension: PublishingExtension,
        implementations: NamedDomainObjectProvider<Configuration>
    )
}