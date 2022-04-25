package buildPublishConfigurator

import pom.context.IPomContext
import libraryMetaData.ILibraryMetaData
import repositoryConfigurator.IPublishRepositoryConfigurator

/**
 * An implementation of this interface is responsible for collecting all necessary information to configure the
 * publishing build for a release, so it can be composed after everything is collected.
 */
interface IBuildPublishConfigurator<T : IPomContext> {
    val publishRepositoryConfigurator: IPublishRepositoryConfigurator
    val libraryMetaData: ILibraryMetaData
    val transform: (T.() -> Unit)?
    val pomContext: T
}
