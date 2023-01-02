// include(":app", ":shared")

//comment app,app-java,and shared if you want to release sdk

rootProject.name = "KaMPKit"

enableFeaturePreview("VERSION_CATALOGS")

//development
// include(":app")
// include(":sdk")
// include(":app-java")
// include(":shared")
// project(":shared").projectDir = File("../kmm-shared/shared")


//release sdk
include(":sdk")

//release shared
// include(":shared")
//if you want to publish it from another repo
// project(":shared").projectDir = File("../kmm-shared/shared")