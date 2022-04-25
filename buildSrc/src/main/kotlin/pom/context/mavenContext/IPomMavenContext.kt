package pom.context.mavenContext

import pom.context.IPomContext
import pom.context.IPomDependenciesContext
import pom.context.IPomLicenseContext
import pom.context.IPomPackagingContext
import pom.context.IPomProjectInfosContext
import pom.context.IPomMavenSettingsContext

/**
 * An implementation of this interface adds all relevant information to the pom.xml file necessary for publishing to Maven Central Repository
 */
interface IPomMavenContext : IPomContext, IPomDependenciesContext, IPomLicenseContext, IPomPackagingContext,
    IPomProjectInfosContext, IPomMavenSettingsContext