package pom.context

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import pom.model.PackagingType
import pom.model.Developer
import pom.model.License
import pom.model.SourceCodeManagement
import pom.context.mavenContext.IPomMavenContext
import pom.context.githubPackagesContext.IPomGithubPackagesContext

/**
 * This class is responsible for holding all the information on what to write into the pom.xml file
 */
class PomContext : IPomContext, IPomMavenSettingsContext, IPomLicenseContext, IPomPackagingContext, IPomProjectInfosContext, IPomMavenContext, IPomGithubPackagesContext {
    override var packagingType: PackagingType? = PackagingType.JAR
    override lateinit var groupId: String
    override lateinit var artifactId: String
    override fun addDependencies(namedDomainObjectProvider: NamedDomainObjectProvider<Configuration>) {
        dependencies = namedDomainObjectProvider.get().allDependencies
    }

    override var dependencies: DependencySet? = null
    override var hasApacheLicense: Boolean = false
    override var hasMITLicense: Boolean = false
    override val licenses: List<License> = mutableListOf()
    override fun addLicense(name: String, url: String) {
        (licenses as MutableList).add(License(name, url))
    }

    override var projectDescription: String? = null
    override var projectUrl: String? = null
    override val developers: List<Developer> = mutableListOf()
    override fun addDeveloper(name: String, email: String, organization: String, organizationUrl: String) {
        (developers as MutableList).add(Developer(name, email, organization, organizationUrl))
    }

    override var hasDistributionManagement: Boolean = false
    override var hasNexusStagingPlugin: Boolean = false
    override var hasSources: Boolean = false
    override var hasJavaDoc: Boolean = false
    override var sourceCodeManagement: SourceCodeManagement? = null
}