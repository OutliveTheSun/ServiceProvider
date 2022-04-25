package pom.context

import pom.model.Developer

/**
 * An implementation of this interface adds project information such as the project name, e-mail and organization to the pom.xml file
 */
interface IPomProjectInfosContext: IPomContext {
    var projectDescription: String?
    var projectUrl: String?
    val developers: List<Developer>
    fun addDeveloper(
        name: String,
        email: String,
        organization: String,
        organizationUrl: String
    )
}