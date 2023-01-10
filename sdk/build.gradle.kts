plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("maven-publish")
  id("signing")
}

android {
  // defaultConfig {
  //   multiDexEnabled =  true
  // }
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
    isAbortOnError = false
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

  implementation("androidx.core:core-ktx:1.8.0")
  implementation("androidx.appcompat:appcompat:1.5.0")
  implementation("com.google.android.material:material:1.6.1")
  // implementation(project(mapOf("path" to ":shared")))
  // testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
  // implementation("io.sentry:sentry:6.6.0")
  // api(project(":shared"))
  // api("com.github.AlifGarindra.KampKitShared:kampkitshared:1.3.3")
  api("id.ottodigital.sdk:ppob.shared:0.0.2")
  // api("com.alifg.libraries:kampkitshared:1.1.4")
  implementation(libs.bundles.app.ui)
  implementation(libs.multiplatformSettings.common)
  implementation(libs.kotlinx.dateTime)
  coreLibraryDesugaring(libs.android.desugaring)
  implementation(libs.koin.android)
  implementation(libs.koin.compose)
  testImplementation(libs.junit)

  implementation("io.sentry:sentry-android:6.4.0")
}

// afterEvaluate{
//   publishing {
//     publications {
//       create<MavenPublication>("gpr") {
//         afterEvaluate {
//           groupId = "com.alifg.libraries"
//           artifactId = "testkampkit"
//           version = "1.2.5"
//           // artifact("$projectDir/libs/sharedkampkit.aar"){
//           //   classifier = "sharedkampkit"
//           //   extension = "aar"
//           // }
//           if (plugins.hasPlugin("java")) {
//             from(components["java"])
//           } else if (plugins.hasPlugin("android-library")) {
//             from(components["release"])
//           }
//         }
//         repositories {
//           maven {
//             url = uri("https://maven.pkg.github.com/AlifGarindra/TestKampKit")
//             credentials {
//               username = "AlifGarindra"
//               password = "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"
//             }
//           }
//         }
//       }
//     }
//   }
// }

// afterEvaluate{
//   publishing {
//     publications {
//       create<MavenPublication>("maven") {
//         afterEvaluate {
//           groupId = "id.ottodigital.sdk"
//           artifactId = "ppobsdk"
//           version = "0.0.1"
//           // groupId = "com.alifg.libraries"
//           // artifactId = "testkampkit"
//           // version = "2.0.1"
//           // artifact("$projectDir/libs/sharedkampkit.aar"){
//           //   classifier = "sharedkampkit"
//           //   extension = "aar"
//           // }
//           if (plugins.hasPlugin("java")) {
//             from(components["java"])
//           } else if (plugins.hasPlugin("android-library")) {
//             from(components["release"])
//           }
//         }
//         // repositories {
//         //   maven {
//         //     url = uri("https://maven.pkg.github.com/AlifGarindra/TestKampKit")
//         //     credentials {
//         //       username = "AlifGarindra"
//         //       password = "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"
//         //     }
//         //   }
//         // }
//       }
//     }
//   }
// }


afterEvaluate{
  publishing {
    publications {
      create<MavenPublication>("maven") {
        afterEvaluate {
          groupId = "id.ottodigital.sdk"
          artifactId = "ppob"
          version = "0.0.3"
          // artifact("$projectDir/libs/sharedkampkit.aar"){
          //   classifier = "sharedkampkit"
          //   extension = "aar"
          // }
          if (plugins.hasPlugin("java")) {
            from(components["java"])
          } else if (plugins.hasPlugin("android-library")) {
            from(components["release"])
          }
        }
        repositories {
          maven {
            url = uri("https://gitlab.pede.id/api/v4/projects/821/packages/maven")
            name = "GitLab"
            credentials(HttpHeaderCredentials::class) {
              name = "Deploy-Token"
              value = "wMGh2iqairzRZsGzvqbK"
            }
            authentication {
              create<HttpHeaderAuthentication>("header")
            }
          }
        }
      }
    }
  }
}
