package pom.context

import pom.model.SourceCodeManagement

/**
 * An implementation of this interface adds information such as available jar files, distribution management information etc. to the pom.xml file
 */
interface IPomMavenSettingsContext : IPomContext {
    var hasDistributionManagement: Boolean
    var hasNexusStagingPlugin: Boolean
    var hasSources: Boolean
    var hasJavaDoc: Boolean
    var sourceCodeManagement: SourceCodeManagement?
}