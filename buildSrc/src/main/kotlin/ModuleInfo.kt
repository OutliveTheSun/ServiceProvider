import org.gradle.api.Project

/**
 * This class provides information on the module (library)
 */
class ModuleInfo(private val module: Project) {

    fun getArtifactId(): String {
        return module.name.toLowerCase()
    }

    fun getVersionNumber(): String {
        return ModuleVersionNumbers.dependencyVersions[module.name] ?: throw RuntimeException("${this.javaClass.simpleName}: No version number found for ${getArtifactId()}")
    }
}