package com.squareup.workflow1.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.squareup.workflow1.lint.WorkflowIssueRegistry.Companion.CAPTURED_STATE
import org.junit.Test

class CapturedStateDetectorTest {

  @Test fun go() {
    lint()
        .files(kotlin("""
          |package foo
          |
          |import com.squareup.workflow1.StatefulWorkflow 
          |
          |class MyWorkflow : StatefulWorkflow<String, String, String, String> {
          |  override fun initialState(props: String, snapshot: Snapshot?) = TODO()
          |  override fun snapshotSTate(state: String) = null
          |  
          |  override fun render(
          |    props: String,
          |    state: String,
          |    context: RenderContext,
          |  ) {
          |    context.eventHandler { if (state == "") TODO() }
          |  }
          |}
        """.trimMargin()))
        .issues(CAPTURED_STATE)
        .run()
        .expectClean()
  }
}
