package com.country.information

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.country.information.networking.model.response.CountryInformation
import com.country.information.networking.model.response.Rows
import com.country.information.uiscreens.MainActivity
import com.country.information.uiscreens.adapater.CustomAdapter
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class FragmentAndReyclerViewTest {
    // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun verifyActivityIsLaunched() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun verifyFragmentIsLaunched() {
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.mframeLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun verify_isListFragmentVisible_onSuccessResponse() {
        onView(withId(R.id.button)).perform(click());

        Thread.sleep(DELAY_IN_MILLIS)

        onView(withId(R.id.recyclerView_countrydetails)).check(matches(isDisplayed()))
        onView(withId(R.id.textView)).check(matches(not(isDisplayed())))

    }

    @Test
    fun verify_isErrorMessageVisible_onErrorResponse() {
        onView(withId(R.id.button)).perform(click())

        onView(withId(R.id.textView)).check(matches(isDisplayed()))
    }

    @Test
    fun verify_ListItemIsClicked() {
        onView(withId(R.id.button)).perform(click())
        // assuming a 5 second delay to fetch response from server and then validate list item is cliked
        Thread.sleep(DELAY_IN_MILLIS)
        onView(withId(R.id.recyclerView_countrydetails))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<CustomAdapter.CountryViewHolder>(
                        0,
                        clickItemWithId(R.id.txt_title)
                    )
            )
    }

    @Test
    fun verify_ListItemIsDisplayed() {
        onView(withId(R.id.button)).perform(click())
        // assuming a 5 second delay to fetch response from server and then validate list item is cliked
        Thread.sleep(DELAY_IN_MILLIS)
        onData(`is`(instanceOf(String::class.java)))
            .inAdapterView(withId(R.id.recyclerView_countrydetails))
            .onChildView(withText(countrydetails.title))
            .check(matches(isDisplayed()))

    }


    fun clickItemWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id) as View
                v.performClick()
            }
        }
    }


    val countrydetails = CountryInformation(
        "About Canada",
        listOf(
            Rows(
                title = "Beavers",
                description = "Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony",
                imageHref = "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg"
            )
        )

    )
    companion object{
        val DELAY_IN_MILLIS = 8000L
    }
}
