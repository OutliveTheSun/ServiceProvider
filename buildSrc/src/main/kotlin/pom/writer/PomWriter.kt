package pom.writer

import ModuleInfo
import VersionNumbers
import gradlePropertiesReader.GradlePropertiesReader
import groovy.util.Node
import groovy.xml.QName
import ifNotEmpty
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.publish.maven.MavenPom
import pom.context.*

/**
 * This class is responsible for writing information contained or defined by the [IPomContext] into the pom.xml file
 */
class PomWriter(private val project: Project, private val pomContext: IPomContext) {
    fun write(mavenPom: MavenPom) {
        with(mavenPom) {
            if (pomContext is IPomPackagingContext) {
                packaging = pomContext.packagingType?.readableName
            }
            name.set("${pomContext.groupId}:${pomContext.artifactId}")
            if (pomContext is IPomProjectInfosContext) {
                pomContext.projectDescription?.apply { description.set(this) }
                pomContext.projectUrl?.apply { url.set(this) }
            }
            if (pomContext is IPomLicenseContext) {
                pomContext.licenses.ifNotEmpty {
                    licenses {
                        pomContext.licenses.forEach { license ->
                            license {
                                name.set(license.name)
                                url.set(license.url)
                            }
                        }
                    }
                }
            }
            if (pomContext is IPomProjectInfosContext) {
                pomContext.developers.ifNotEmpty {
                    developers {
                        pomContext.developers.forEach { developer ->
                            developer {
                                name.set(developer.name)
                                email.set(developer.email)
                                organization.set(developer.organization)
                                organizationUrl.set(developer.organizationUrl)
                            }
                        }
                    }
                }
            }
            if (pomContext is IPomMavenSettingsContext) {
                pomContext.sourceCodeManagement?.apply {
                    scm {
                        connection.set(this@apply.connection)
                        developerConnection.set(this@apply.developerConnection)
                        url.set(this@apply.url)
                    }
                }
            }
            pomContext.dependencies?.apply {
                addDependenciesToPom(this@apply)
            }
        }
    }

    private fun MavenPom.addDependenciesToPom(dependencies: DependencySet) {
        if (dependencies.isEmpty()) {
            return
        }
        withXml {
            //find automatically created dependencies node and delete it
            deleteExistingDependencyNode(asNode())

            //add own dependencies
            addConfiguredDependencyNode(dependencies, asNode())
        }
    }

    private fun addConfiguredDependencyNode(dependencies: DependencySet, rootNode: Node) {
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
                groupId = GradlePropertiesReader(project).getStringProperty("groupId")
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
        val dependenciesNode = rootNode.findNodeByName("dependencies")
        if (dependenciesNode != null) {
            rootNode.remove(dependenciesNode)
        }
    }

    private fun Node.findNodeByName(nodeName: String): Node? =
        this.children().find { (((it as Node).name()) as QName).localPart == nodeName } as Node
}