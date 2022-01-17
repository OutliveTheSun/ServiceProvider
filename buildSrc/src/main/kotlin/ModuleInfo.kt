import org.gradle.api.Project

class ModuleInfo(private val module: Project) {

    fun getArtifactId(): String {
        return module.name.toLowerCase()
    }

    fun findVersionNumber(): String {
        return VersionNumbers.dependencyVersions[module.name] ?: throw RuntimeException("${this.javaClass.simpleName}: No version number found for ${getArtifactId()}")
    }
}