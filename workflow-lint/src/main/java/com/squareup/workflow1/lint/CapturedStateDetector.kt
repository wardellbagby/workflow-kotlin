@file:Suppress("UnstableApiUsage")

package com.squareup.workflow1.lint

import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.visitor.AbstractUastVisitor
import org.jetbrains.uast.visitor.UastVisitor

class CapturedStateDetector : Detector(), SourceCodeScanner {
  override fun getApplicableMethodNames(): List<String> {
    return listOf("eventHandler", "action")
  }

  override fun visitMethodCall(
    context: JavaContext,
    call: UCallExpression,
    method: PsiMethod
  ) {
    val evaluator = context.evaluator

    if ("eventHandler" == call.methodName &&
        evaluator.isMemberInClass(method, "com.squareup.workflow1.BaseRenderContext")) {
      call.valueArguments.last().accept(object : AbstractUastVisitor() {
        override fun visitBlockExpression(node: UBlockExpression): Boolean {
          return true
        }
      })
    }
  }
}
