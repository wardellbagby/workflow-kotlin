plugins {
  kotlin("multiplatform")
  kotlin("native.cocoapods")
}

version = 1.0

kotlin {
  jvm()
  js {
    browser()
  }
  iosX64()

  sourceSets {
    all {
      languageSettings.apply {
        useExperimentalAnnotation("kotlin.RequiresOptIn")
        useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
      }
    }
  }

  cocoapods {
    homepage = "https://square.github.io/workflow/"
    summary = "Shared code for multiplatform tictactoe sample"
  }

  sourceSets["commonMain"].dependencies {
    api(project(":workflow-core"))
    api(project(":workflow-ui:backstack-common"))
    api(project(":workflow-ui:modal-common"))
    api(project(":samples:containers:common"))

    implementation(Dependencies.Kotlin.Coroutines.core)
    implementation(Dependencies.stately)
    implementation(Dependencies.kermit)
  }

  sourceSets["jvmTest"].dependencies {
    implementation(Dependencies.Test.hamcrestCore)
    implementation(Dependencies.Test.junit)
    implementation(Dependencies.Test.truth)
    implementation(project(":workflow-testing"))
  }
}
