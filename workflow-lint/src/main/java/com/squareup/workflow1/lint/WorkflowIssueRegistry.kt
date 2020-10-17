@file:Suppress("UnstableApiUsage")

package com.squareup.workflow1.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity
import java.util.EnumSet

class WorkflowIssueRegistry : IssueRegistry() {
  override val issues: List<Issue> = listOf(CAPTURED_STATE)

  companion object {
    val CAPTURED_STATE = Issue.create(
        id = "WorkflowCapturedState",
        briefDescription = "Do not capture state",
        explanation = "Because that's bad",
        category = Category.CORRECTNESS,
        priority = 5,
        severity = Severity.ERROR,
        implementation = Implementation(
            CapturedStateDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)
        )
    )
  }
}
