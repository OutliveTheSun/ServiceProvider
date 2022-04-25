package buildProcessConfigFacade

class PublishingNotSetUpException :
    RuntimeException("Publishing was not set up for this project. Call ${IBuildProcessConfigFacade::setupPublishing.name}(this@publishing, configurations.implementation) in function 'publishing' in build.gradle-file")