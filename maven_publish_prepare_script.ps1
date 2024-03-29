﻿#-------------------
#Description:
# This script is responsible for setting up the publishing on Maven Central
#Step-by-step:
# The script:
# 1) checks if the "maven-publish" folder exists and creates it if not.
# 2) downloads/updates the repository which holds our config files from Github into the "maven-publish" folder.
# 3) adds the "gradle.properties" file from the config folder to the users directory. It also replaces the placeholders with the ones from the user.
# 4) checks if the maven publish private key file exists and show warning if not. It has to exist in the "maven-publish" folder. If not, it has to be added manually.
# 5) copies the project template file (zip file) from the config folder into Intellij-Idea-Version folder.
# 6) clones the project template repository (to have an up-to-date version of our publishing library files for an existing library).
# 7) updates the buildScr folder in the project that the script is executed from
# Conditions:
# The script needs to run inside the project that needs the buildSrc folder updating.
#-------------------
function createDirectoryIfNotExists{
    #check if folder exists and otherwise create it
    if(-not(Test-Path $args[0])){
        mkdir $args[0]
        Write-Host "Path '$args[0]' did not exist and was created" -BackgroundColor Green -ForegroundColor DarkBlue
    }
}
function deleteDirectory{
    Remove-Item $args[0] -Recurse -Force -Confirm:$false
    Write-Host "Folder '$args[0]' was deleted." -BackgroundColor Green -ForegroundColor DarkBlue
}

function cloneOrUpdateRepository{
    $sourcePath = $args[0]
    $filePath = $args[1]
    $repoUrl = -join ("https://github.com/OutliveTheSun/", $args[2])
    
    cd $sourcePath
    #check if folder exists -> meaning the repository was downloaded
    $repositoryExists = Test-Path $filePath
    if(-not($repositoryExists)){
        git clone $repoUrl
        Write-Host "Repository '$repoUrl' was cloned" -BackgroundColor Green -ForegroundColor DarkBlue
    }
    cd $filePath
    if($repositoryExists){
        git pull origin master
        Write-Host "Repository '$repoUrl' was pulled" -BackgroundColor Green -ForegroundColor DarkBlue
    }
}

function copyGradlePropertiesAndReplaceUserPathPlaceholder{
    cd $configGitRepoFilePath
    $gradleUserPathFormatted = $env:USERPROFILE -replace '\\', '\\'
    (Get-Content $gradlePropertiesFileName) -replace '<user_folder>', $gradleUserPathFormatted | Set-Content $gradlePropertiesFilePath/$gradlePropertiesFileName
    Write-Host "'$gradlePropertiesFileName' copied into '$gradlePropertiesFilePath'" -BackgroundColor Green -ForegroundColor DarkBlue
}

function checkForMavenPrivateKey{
    #check if maven private key file exists -> if not show warning
    $keyRingFilePath = Get-Content $gradlePropertiesFilePath/$gradlePropertiesFileName | Select-String -Pattern "signing.secretKeyRingFile" | %{$_.Line.Split("=")[1]} 
    if(-not(Test-Path $keyRingFilePath)){
        Write-Host "There is no private key ring file here: '$keyRingFilePath'. You have to add it from where you got this running script file from." -BackgroundColor Red
    }
}

function copyProjectTemplate{
    Get-ChildItem -Path $jetBrainsPath -Directory | Where-Object { $_.name -Like "Idea*" } | ForEach-Object{ copyTemplateProjectFileIntoPath $configGitRepoFilePath $templateProjectFilename $jetBrainsPath $_.name }
}

function copyTemplateProjectFileIntoPath{
    $sourcePath = Join-Path $args[0] $args[1]
    $destinationPath = Join-Path $args[2] $args[3]
    $destinationPath = Join-Path $destinationPath projectTemplates
    createDirectoryIfNotExists $destinationPath
    Copy-Item $sourcePath $destinationPath
    Write-Host "'$templateProjectFilename' copied into '$destinationPath'" -BackgroundColor Green -ForegroundColor DarkBlue
}

function updateBuildSrcFolderFromRepo{
    $buildScrFolderExists = (Test-Path $targetBuildSrcFolderPath)
    if($buildScrFolderExists){
        #backup the ModuleVersionNumbers file
        $moduleVersionNumbersFilePath = Join-Path $targetBuildSrcFolderPath src\main\kotlin\ModuleVersionNumbers.kt
        $backupModuleVersionsNumberFileContent = Get-Content $moduleVersionNumbersFilePath
        
        deleteDirectory $targetBuildSrcFolderPath
    }
    addBuildSrcFiles
    if($buildScrFolderExists){
        #restore the ModuleVersionNumbers file
        $backupModuleVersionsNumberFileContent | Set-Content $moduleVersionNumbersFilePath
    }
}


function addBuildSrcFiles{
    $sourcePathBuildSrcFolder = Join-Path $pathToSaveTo $templateGitRepoName
    $sourcePathBuildSrcFolder = Join-Path $sourcePathBuildSrcFolder buildSrc
    Copy-Item $sourcePathBuildSrcFolder $initialPathTheScripRunsIn -Recurse
    Write-Host "'$initialPathTheScripRunsIn' updated from '$sourcePathBuildSrcFolder'" -BackgroundColor Green -ForegroundColor DarkBlue
}

#-----------------------------------------------------------------------
$initialPathTheScripRunsIn = Split-Path $MyInvocation.myCommand.Path -Parent
$pathToSaveTo = Join-Path $env:USERPROFILE maven_publish
$targetBuildSrcFolderPath = Join-Path $initialPathTheScripRunsIn buildSrc
#config git repository
$configGitRepoName = "PublishLibraryConfigFiles"
$configGitRepoFilePath = Join-Path $pathToSaveTo $configGitRepoName
#template git repository
$templateGitRepoName = "PublishLibraryTemplateProject"
#gradle.properties file
$gradlePropertiesFileName = ".\gradle.properties"
$gradlePropertiesFilePath = Join-Path $env:USERPROFILE .gradle\
#JetBrains
$jetBrainsPath = Join-Path $env:APPDATA JetBrains
$templateProjectFilename = "PublishLibraryTemplateProject.zip"


createDirectoryIfNotExists $pathToSaveTo
#Download/Update the repository that our config files are saved in from Github
cloneOrUpdateRepository $pathToSaveTo $configGitRepoFilePath $configGitRepoName
#Add gradle.properties to user directory
copyGradlePropertiesAndReplaceUserPathPlaceholder
#Check if the maven publish private key file exists and show warning if not
checkForMavenPrivateKey
#Copy project template containing all publishing information into every Intellij-Idea-Version folder
copyProjectTemplate
#Clone the project template repository (to have an up-to-date version of our publishing library files for an existing library)
cloneOrUpdateRepository $pathToSaveTo $templateGitRepoName $templateGitRepoName
updateBuildSrcFolderFromRepo
#switch back to the starting directory
cd $initialPathTheScripRunsIn