package android.app

import android.app.Dialogs.getOnShowListener
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.squareup.workflow1.ui.WorkflowViewStubLifecycleActivity
import com.squareup.workflow1.ui.internal.test.inAnyView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class DialogsTest {
  @Rule @JvmField val scenarioRule =
    ActivityScenarioRule(WorkflowViewStubLifecycleActivity::class.java)
  private val scenario get() = scenarioRule.scenario

  @Test fun canGetNullOnShowListener() {
    scenario.onActivity { activity ->
      val dialog = AlertDialog.Builder(activity).create()
      assertThat(getOnShowListener(dialog)).isNull()
    }
  }

  @Test fun canSetAndGetOnShowListener() {
    var calledOuter = false
    var calledInner = false

    scenario.moveToState(Lifecycle.State.RESUMED)
    scenario.onActivity { activity ->
      val dialog = Dialog(activity).apply {
        val textView = TextView(activity).apply {
          layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
          text = "Bite me"
        }
        setContentView(textView)
        window!!.setLayout(WRAP_CONTENT, WRAP_CONTENT)
      }
      dialog.setOnShowListener { calledInner = true }
      val inner = getOnShowListener(dialog)
      dialog.setOnShowListener {
        inner?.onShow(it)
        calledOuter = true
      }

      dialog.show()
    }

    inAnyView(withText("Bite me")).check(matches(isDisplayed()))

    assertThat(calledInner).isTrue()
    assertThat(calledOuter).isTrue()
  }
}
