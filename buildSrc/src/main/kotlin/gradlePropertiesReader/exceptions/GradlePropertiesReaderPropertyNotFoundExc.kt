package gradlePropertiesReader.exceptions

class GradlePropertiesReaderPropertyNotFoundExc(key: String)
    : RuntimeException("There is no property with key '$key' in gradle.properties-file. Add the property to gradle.properties in folder ${System.getProperty("user.home")}\\.gradle\\ . If the file does not yet exist, copy it from 'https://github.com/OutliveTheSun/GithubPackagePublishing/gradle.properties]'")