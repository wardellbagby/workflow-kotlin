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
  }
}

dependencies {
  api(project(":workflow-ui:core-common"))
  api(project(":workflow-runtime"))

  api("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.236-kotlin-1.5.30")
  api("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.236-kotlin-1.5.30")


  implementation(Dependencies.Kotlin.Coroutines.core)
  implementation(npm("react", "17.0.2"))
  implementation(npm("react-dom", "17.0.2"))
}

afterEvaluate {
  rootProject.extensions.configure<NodeJsRootExtension> {
    versions.webpackDevServer.version = "^4.0.0"
  }
}
