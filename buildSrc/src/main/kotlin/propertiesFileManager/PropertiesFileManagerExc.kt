package propertiesFileManager

import Config

class PropertiesFileManagerExc(fileName: String, filePath: String) :
    RuntimeException("Properties file '$fileName' does not exist in '$filePath'. 1) If the file is located elsewhere, go to file 'buildSrc/src/main/kotlin/${Config::class.simpleName}.kt' and change the parameter path definition 'githubConfigFilePath' to the correct path. 2) If the file does not yet exist, copy it from 'https://github.com/OutliveTheSun/GithubPackagePublishing/blob/master/forGithubProperties/config.properties] into directory '$filePath'.")
