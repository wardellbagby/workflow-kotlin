import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
  kotlin("js")
}

repositories {
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
  maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
  maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
  mavenCentral()
}

kotlin {
  js {
    browser {
      webpackTask {
        cssSupport.enabled = true
      }
      runTask {
        cssSupport.enabled = true
      }
    }
    binaries.executable()
  }
}

dependencies {
  implementation(project(":samples:tictactoe-multiplatform:shared"))
  implementation(project(":workflow-ui:core-react"))
  implementation("org.jetbrains.kotlin-wrappers", "kotlin-styled", "5.3.0-pre.236-kotlin-1.5.30")
  implementation("com.ccfraser.muirwik:muirwik-components:0.9.1")
}

afterEvaluate {
  rootProject.extensions.configure<NodeJsRootExtension> {
    versions.webpackDevServer.version = "^4.0.0"
  }
}
