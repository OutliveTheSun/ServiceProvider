package libraryMetaData

/**
 * This class is responsible for providing the metadata information for a snapshot(=test) library for Maven
 */
class MavenSnapshotLibraryMetaData(private val libraryMetadata: ILibraryMetaData) :
    ILibraryMetaData by libraryMetadata {
    override fun getArtifactId(): String {
        return "${libraryMetadata.getArtifactId()}-SNAPSHOT"
    }

    override fun getVersionNumber(): String {
        return "${libraryMetadata.getVersionNumber()}-SNAPSHOT"
    }
}