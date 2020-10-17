plugins {
  `java-library`
  kotlin("jvm")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_7
  targetCompatibility = JavaVersion.VERSION_1_7
}

apply(from = rootProject.file(".buildscript/configure-maven-publish.gradle"))

dependencies {
  compileOnly(Dependencies.Annotations.intellij)
  compileOnly(Dependencies.Lint.api)

  api(Dependencies.Kotlin.Stdlib.jdk7)

  testImplementation(Dependencies.Lint.lint)
  testImplementation(Dependencies.Lint.tests)

  // Figure I need this s.t. the test source can be parsed. Why can't gradle resolve it?
  testImplementation(project(":workflow-core"))
}

val jar: Jar by tasks.getting(Jar::class) {
  manifest {
    attributes(mapOf("Lint-Registry-v2" to "com.squareup.workflow1.lint.WorkflowIssueRegistry"))
  }
}
