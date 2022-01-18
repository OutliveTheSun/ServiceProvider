package com.outlivethesun.serviceprovider.internal.classloader

internal sealed class ReflectionInfoException(override val message: String?) : RuntimeException(message)
internal class ReflectionInfoMissingPackageException(
    private val interfaceName: String,
    private val runningProgramName: String,
    override val message: String? = "Error when determining the classes that implement the interface '$interfaceName'. " +
            "Could not determine the root package of the running program '$runningProgramName'. " +
            "Check for existing package declaration in '$runningProgramName'. E.g. 'package com.outlivethesun.demo'"

) : ReflectionInfoException(message)