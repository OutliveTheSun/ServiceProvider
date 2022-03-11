import org.gradle.kotlin.dsl.provideDelegate
import propertiesFileManager.PropertiesFileManager

object Config {
    const val groupId = "com.outlivethesun"
    const val githubURL = "https://maven.pkg.github.com/outlivethesun"
    private var githubConfigFilePath = "${System.getProperty("user.home")}\\GithubPackagePublishingConfig\\"
    private const val githubConfigFileName = "config.properties"
    private val propertiesFileManager by lazy { PropertiesFileManager(githubConfigFilePath, githubConfigFileName) }
    val githubLibraryUser: String? by lazy { propertiesFileManager.readValueFromFile("githubLibraryUser") }
    val githubLibraryKeyPublish: String? by lazy { propertiesFileManager.readValueFromFile("githubLibraryKeyPublish") }
    val githubLibraryKeyConsume: String? by lazy { propertiesFileManager.readValueFromFile("githubLibraryKeyConsume") }
}