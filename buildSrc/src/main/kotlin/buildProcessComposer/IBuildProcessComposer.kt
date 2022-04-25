package buildProcessComposer

/**
 * An implementation of this interface is responsible for putting together and configuring all components necessary for publishing.
 * e.g. adding additional jar files or adjusting the pom-file.
 */
interface IBuildProcessComposer {
    fun compose()
}