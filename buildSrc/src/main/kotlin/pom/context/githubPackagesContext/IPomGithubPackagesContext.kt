package pom.context.githubPackagesContext

import pom.context.IPomDependenciesContext
import pom.context.IPomContext

/**
 * An implementation of this interface adds all relevant information to the pom.xml file necessary for publishing a GithubPackage
 */
interface IPomGithubPackagesContext : IPomContext, IPomDependenciesContext