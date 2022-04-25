package buildProcessComposer

import buildPublishConfigurator.IBuildPublishConfigurator
import jar.JarFilesConfigurator
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import pom.context.IPomContext
import pom.writer.PomWriter
import libraryMetaData.ILibraryMetaData
import gradlePropertiesReader.GradlePropertiesReader

class BuildProcessComposer<T : IPomContext>(
    private val project: Project,
    private val publishingExtension: PublishingExtension,
    private val implementations: NamedDomainObjectProvider<Configuration>,
    private val buildPublishConfigurator: IBuildPublishConfigurator<T>
) :
    IBuildProcessComposer {
    override fun compose() {
        configureAdditionalJars()
        publishingExtension.publications {
            create<MavenPublication>("mavenJava") {
                setLibraryMetadata(buildPublishConfigurator.libraryMetaData)
                addJarFilesToPublication()
                pom {
                    adjustPomFile(this, implementations, buildPublishConfigurator.libraryMetaData)
                }
            }
        }
        publishingExtension.repositories {
            buildPublishConfigurator.publishRepositoryConfigurator.addPublishRepository(this)
        }
    }

    private fun configureAdditionalJars() {
        JarFilesConfigurator(project).configure()
    }

    private fun adjustPomFile(
        mavenPom: MavenPom,
        implementations: NamedDomainObjectProvider<Configuration>,
        libraryMetadata: ILibraryMetaData
    ) {
        buildPublishConfigurator.pomContext.apply {
            addDependencies(implementations)
            groupId = GradlePropertiesReader(project).getStringProperty("groupId")
            artifactId = libraryMetadata.getArtifactId()
        }
        //TODO: Verify?
        PomWriter(project, buildPublishConfigurator.pomContext).write(mavenPom)
    }

    private fun MavenPublication.addJarFilesToPublication() {
        from(project.components["java"])
    }

    private fun MavenPublication.setLibraryMetadata(libraryMetadata: ILibraryMetaData) {
        groupId = libraryMetadata.getGroupId()
        artifactId = libraryMetadata.getArtifactId()
        version = libraryMetadata.getVersionNumber()
    }

}