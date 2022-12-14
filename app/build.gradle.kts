plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        multiDexEnabled=true
        applicationId = "com.otto.sdk.shared.kampkit"
        minSdk = 19
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packagingOptions {
        resources.excludes.add("META-INF/*.kotlin_module")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    lint {
        isWarningsAsErrors = true
        isAbortOnError = true
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.5.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.google.code.gson:gson:2.7")
    // implementation("com.alifg.libraries:kampkitshared:1.0.4")
    // implementation("com.google.android.material:material:1.6.1")
    implementation(libs.bundles.app.ui)
    // implementation(libs.multiplatformSettings.common)
    implementation(libs.kotlinx.dateTime)
    implementation(project(mapOf("path" to ":sdk")))
    coreLibraryDesugaring(libs.android.desugaring)
    // implementation(libs.koin.android)
    testImplementation(libs.junit)
}
