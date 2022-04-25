package pom.context

import pom.model.License

/**
 * An implementation of this interface adds license information to the pom.xml file
 */
interface IPomLicenseContext: IPomContext {
    var hasApacheLicense: Boolean
    var hasMITLicense: Boolean
    val licenses: List<License>
    fun addLicense(name: String, url: String)
}