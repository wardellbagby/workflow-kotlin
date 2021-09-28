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
    browser()
    binaries.executable()
  }
}

dependencies {
  implementation(project(":samples:hello-workflow-multiplatform:shared"))
  implementation(project(":workflow-ui:core-react"))
}

afterEvaluate {
  rootProject.extensions.configure<NodeJsRootExtension> {
    versions.webpackDevServer.version = "^4.0.0"
  }
}
