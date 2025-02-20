package com.squareup.sample.mainactivity

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.squareup.sample.todo.R
import com.squareup.workflow1.ui.internal.test.inAnyView
import com.squareup.workflow1.ui.internal.test.actuallyPressBack
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoAppTest {

  @Rule @JvmField val scenarioRule = ActivityScenarioRule(ToDoActivity::class.java)
  private val uiDevice by lazy { UiDevice.getInstance(getInstrumentation()) }

  @Before
  fun setUp() {
    uiDevice.unfreezeRotation()
  }

  @After
  fun tearDown() {
    uiDevice.unfreezeRotation()
  }

  @Test fun navigatesToListAndBack_portrait() {
    val isPortrait = uiDevice.displayWidth < uiDevice.displayHeight
    if (!isPortrait) uiDevice.setOrientationLeft()

    inAnyView(withText("Daily Chores"))
      .check(matches(allOf(isDisplayed())))
      .perform(click())
    inAnyView(withId(R.id.item_container))
      .check(matches(isDisplayed()))
    actuallyPressBack()
    inAnyView(withId(R.id.todo_lists_container))
      .check(matches(isDisplayed()))
  }

  @Test fun showsOverviewAndDetail_landscape() {
    val isPortrait = uiDevice.displayWidth < uiDevice.displayHeight
    if (isPortrait) uiDevice.setOrientationLeft()

    inAnyView(withText("Daily Chores"))
      .check(matches(allOf(isDisplayed())))
      .perform(click())
    inAnyView(withId(R.id.item_container))
      .check(matches(isDisplayed()))
      inAnyView(withId(R.id.todo_lists_container))
      .check(matches(isDisplayed()))
  }
}
