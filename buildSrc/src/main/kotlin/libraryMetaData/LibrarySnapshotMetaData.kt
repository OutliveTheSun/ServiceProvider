package libraryMetaData

/**
 * This class is responsible for providing the metadata information for a snapshot(=test) library
 */
class LibrarySnapshotMetaData(private val libraryMetadata: ILibraryMetaData) : ILibraryMetaData {
    override fun getGroupId(): String {
        return libraryMetadata.getGroupId()
    }

    override fun getArtifactId(): String {
        return "${libraryMetadata.getArtifactId()}-SNAPSHOT"
    }

    override fun getVersionNumber(): String {
        return "${libraryMetadata.getVersionNumber()}-SNAPSHOT"
    }
}