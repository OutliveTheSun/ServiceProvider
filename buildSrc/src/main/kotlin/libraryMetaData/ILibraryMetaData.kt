package libraryMetaData

/**
 * An implementation of this interface is responsible for providing metadata for a library
 */
interface ILibraryMetaData {
    fun getGroupId(): String
    fun getArtifactId(): String
    fun getVersionNumber(): String
}