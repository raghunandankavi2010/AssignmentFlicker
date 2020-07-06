package com.example.flcikersample.ui

import android.view.KeyEvent
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.flcikersample.R
import com.task.utils.EspressoIdlingResource
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    private val testSearchString = "Mobile"

    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    private var mIdlingResource: IdlingResource? = null

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }


    @Test
    fun testSearch() {

        onView(withId(R.id.search_image)).perform(click())
        onView(withId(R.id.search_image))
                .perform(typeText(testSearchString))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.list)).check(matches(isDisplayed()))
        onView(withId(R.id.list)).check( RecyclerViewItemCountAssertion(21))


    }

    @Test
    fun testWhenNoNetwork() {

        onView(withId(R.id.search_image)).perform(click())
        onView(withId(R.id.search_image))
            .perform(typeText(testSearchString))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()))


    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister()
        }
    }
}