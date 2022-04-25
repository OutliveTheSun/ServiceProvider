package gradlePropertiesReader

import org.gradle.api.Project
import gradlePropertiesReader.exceptions.GradlePropertiesReaderPropertyNotFoundExc
import gradlePropertiesReader.exceptions.GradlePropertiesReaderTypeExc

/**
 * This class is responsible for reading properties from the gradle.properties file located in the user directory under "/.gradle"
 */
class GradlePropertiesReader(private val project: Project) {
    fun getStringProperty(key: String): String {
        val value = project.properties[key] ?: throw GradlePropertiesReaderPropertyNotFoundExc(key)
        return if (value is String) {
            value
        } else {
            throw GradlePropertiesReaderTypeExc(key, String::class)
        }
    }
}