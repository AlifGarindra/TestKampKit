// include(":app", ":shared") develope
include(":app")
rootProject.name = "KaMPKit"

enableFeaturePreview("VERSION_CATALOGS")
include(":sdk")
include(":app-java")
include(":shared")
project(":shared").projectDir = File("../kmm-shared/shared")

