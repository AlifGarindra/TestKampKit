// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    google()
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
    maven("https://jitpack.io")
  }
  dependencies {
    val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")
      as org.gradle.accessors.dm.LibrariesForLibs
    classpath(libs.bundles.gradlePlugins)
    classpath(kotlin("gradle-plugin", libs.versions.kotlin.get()))
    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build gradle files
  }
}

// https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.gradleDependencyUpdate)
}

allprojects {
  repositories {
    google()
    mavenCentral()
    maven { url = uri( "https://jitpack.io" )}
    // maven {
    //   name = "GitHubPackages"
    //   url = uri("https://maven.pkg.github.com/AlifGarindra/KampKitShared")
    //   credentials {
    //     username = "AlifGarindra"
    //     password = "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"
    //   }
    // }
    // maven("https://jitpack.io")
    maven {
      url = uri("https://gitlab.pede.id/api/v4/projects/821/packages/maven")
      name = "GitLab"
      credentials(HttpHeaderCredentials::class) {
        name = "Deploy-Token"
        value = "r2yKiEbXmhQUfiijHXLU"
      }
      authentication {
        create<HttpHeaderAuthentication>("header")
      }
    }
  }
}

subprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    enableExperimentalRules.set(true)
    verbose.set(true)
    filter {
      exclude { it.file.path.contains("build/") }
    }
  }

  afterEvaluate {
    tasks.named("check").configure {
      dependsOn(tasks.getByName("ktlintCheck"))
    }
  }
}

tasks.register<Delete>("clean") {
  delete(rootProject.buildDir)
}
