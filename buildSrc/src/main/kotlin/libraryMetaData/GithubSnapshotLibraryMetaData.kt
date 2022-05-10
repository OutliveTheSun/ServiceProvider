package libraryMetaData

/**
 * This class is responsible for providing the metadata information for a snapshot(=test) library for Github
 */
class GithubSnapshotLibraryMetaData(private val libraryMetadata: ILibraryMetaData) :
    ILibraryMetaData by libraryMetadata {
    override fun getVersionNumber(): String {
        return "${libraryMetadata.getVersionNumber()}-SNAPSHOT"
    }
}