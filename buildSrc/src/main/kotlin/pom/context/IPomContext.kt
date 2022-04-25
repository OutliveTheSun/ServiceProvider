package pom.context

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet

/**
 * An implementation of this interface is responsible for holding all information used to write the pom.xml file
 */
interface IPomContext {
    var groupId: String
    var artifactId: String
    fun addDependencies(namedDomainObjectProvider: NamedDomainObjectProvider<Configuration>)
    val dependencies: DependencySet?
}