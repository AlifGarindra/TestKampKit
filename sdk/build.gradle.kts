plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
}

android {
  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

  // implementation("androidx.core:core-ktx:1.7.0")
  // implementation("androidx.appcompat:appcompat:1.5.0")
  // implementation("com.google.android.material:material:1.6.1")
  // implementation(project(mapOf("path" to ":shared")))
  // testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

  implementation(project(":shared"))
  implementation(libs.bundles.app.ui)
  implementation(libs.multiplatformSettings.common)
  implementation(libs.kotlinx.dateTime)
  coreLibraryDesugaring(libs.android.desugaring)
  implementation(libs.koin.android)
  implementation(libs.koin.compose)
  testImplementation(libs.junit)

  implementation("io.sentry:sentry-android:6.4.0")
}