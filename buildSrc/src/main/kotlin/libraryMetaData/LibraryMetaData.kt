package libraryMetaData

import ModuleInfo
import org.gradle.api.Project
import gradlePropertiesReader.GradlePropertiesReader

class LibraryMetaData(private val project: Project) : ILibraryMetaData {
    private val moduleInfo: ModuleInfo by lazy { ModuleInfo(project) }
    override fun getGroupId(): String {
        return GradlePropertiesReader(project).getStringProperty("groupId")
    }

    override fun getArtifactId(): String {
        return moduleInfo.getArtifactId()
    }

    override fun getVersionNumber(): String {
        return moduleInfo.findVersionNumber()
    }
}