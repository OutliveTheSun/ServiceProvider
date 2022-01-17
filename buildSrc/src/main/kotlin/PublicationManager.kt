import groovy.util.Node
import groovy.xml.QName
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.lang.RuntimeException


class PublicationManager(private val project: Project) {

    init {
        setKotlinCompileVersion()
        configureJavaContainer()
    }

    fun configurePublishing(
        publishingExtension: PublishingExtension,
        implementations: NamedDomainObjectProvider<Configuration>
    ) {
        configurePublicationConfig(publishingExtension, implementations)
        configurePublishingRepository(publishingExtension)
    }

    private fun configurePublicationConfig(
        publishingExtension: PublishingExtension,
        implementations: NamedDomainObjectProvider<Configuration>
    ) {
        val moduleInfo = ModuleInfo(project)
        publishingExtension.publications {
            create<MavenPublication>("bar") {
                groupId = Config.groupId
                artifactId = moduleInfo.getArtifactId()
                version = moduleInfo.findVersionNumber()
                //adds the files (jar/sources jar) to the publication
                from(project.components["java"])

                pom.withXml {
                    replaceDependenciesNodeWithConfigured(asNode(), implementations)
                }
            }
        }
    }

    private fun replaceDependenciesNodeWithConfigured(
        rootNode: Node,
        implementations: NamedDomainObjectProvider<Configuration>
    ) {
        val dependencies = implementations.get().allDependencies
        if (dependencies.isEmpty()) {
            return
        }
        //find automatically created dependencies node and delete it
        deleteExistingDependencyNode(rootNode)

        //add own dependencies
        addConfiguredDependencyNode(rootNode, dependencies)
    }

    private fun addConfiguredDependencyNode(
        rootNode: Node,
        dependencies: DependencySet
    ) {
        val newDependenciesNode = rootNode.appendNode("dependencies")
        dependencies.forEach { dep ->
            var groupId = dep.group
            var artifactId = dep.name
            var version = dep.version

            //add local project dependency
            val rootProject = project.rootProject
            //find the dependencies: implementation(project(":someLib"))
            if (dep.group == rootProject.name) {
                val module = rootProject.subprojects.find { module -> module.name == dep.name }
                    ?: throw RuntimeException("No version could be found for the module \"${dep.name}\"")
                val libVersion = VersionNumbers.dependencyVersions[module.name]
                groupId = Config.groupId
                artifactId = ModuleInfo(module).getArtifactId()
                version = libVersion
            }
            newDependenciesNode.appendNode("dependency").apply {
                appendNode("groupId", groupId)
                appendNode("artifactId", artifactId)
                appendNode("version", version)
            }
        }
    }

    private fun deleteExistingDependencyNode(rootNode: Node) {
        val dependenciesNode = rootNode.children()
            .find { (((it as Node).name()) as QName).localPart == "dependencies" }
        if (dependenciesNode != null) {
            rootNode.remove(dependenciesNode as Node)
        }
    }

    private fun configurePublishingRepository(
        publishingExtension: PublishingExtension
    ) {
        publishingExtension.repositories {
            RepositoryManager(project).addPublicationRepository(this)
        }
    }

    private fun setKotlinCompileVersion() {
        //ensures that lambdas with functional interfaces can be used
        val compileKotlin: KotlinCompile by project.tasks
        compileKotlin.kotlinOptions {
            languageVersion = "1.4"
        }
    }

    private fun configureJavaContainer() {
        project.configure<JavaPluginExtension> {
            //create sources jar -> runs the equivalent task
            withSourcesJar()
            sourceCompatibility = JavaVersion.VERSION_1_7
            targetCompatibility = JavaVersion.VERSION_1_7
        }
    }
}