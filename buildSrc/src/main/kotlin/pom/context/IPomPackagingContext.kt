package pom.context

import pom.model.PackagingType

/**
 * An implementation of this interface adds the packaging type to the pom.xml file
 */
interface IPomPackagingContext : IPomContext{
    var packagingType: PackagingType?
}