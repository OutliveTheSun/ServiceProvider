package gradlePropertiesReader.exceptions

import kotlin.reflect.KClass

class GradlePropertiesReaderTypeExc(key: String, expectedType: KClass<*>)
    : RuntimeException("Property with key '$key' in gradle.properties-file has the wrong type. Property was expected to have type '${expectedType.simpleName}'. Change property '$key' in ${System.getProperty("user.home")}\\.gradle\\gradle.properties to type '${expectedType.simpleName}'")
