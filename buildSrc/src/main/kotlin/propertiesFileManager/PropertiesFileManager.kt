package propertiesFileManager

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class PropertiesFileManager(
    private val filePath: String,
    private val fileName: String
) {
    private val file = getFile()
    private val fileInputStream by lazy { FileInputStream(file) }
    private val fileOutputStream by lazy { FileOutputStream(file) }
    private val readProperties by lazy { loadPropertiesFromInputFile() }

    private fun getFile(): File {
        return File("$filePath/$fileName").apply {
            if (!this.exists()) {
                throw PropertiesFileManagerExc(fileName, filePath)
            }
        }
    }

    private fun loadPropertiesFromInputFile(): Properties {
        Properties().apply {
            load(fileInputStream)
            return this
        }
    }

    fun readValueFromFile(key: String): String? {
        return readProperties.getProperty(key)
    }

    fun writeIntoFile(value: Pair<String, String>) {
        val properties = Properties()
        properties.setProperty(value.first, value.second)
        properties.store(fileOutputStream, "This is the config file containing our Github authentication data")
    }
}