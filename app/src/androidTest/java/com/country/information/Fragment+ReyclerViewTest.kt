package com.country.information

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.country.information.networking.model.response.CountryInformation
import com.country.information.networking.model.response.Rows
import com.country.information.uiscreens.MainActivity
import com.country.information.uiscreens.adapater.CustomAdapter
import com.country.information.utils.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
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

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)

    }

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
        onView(withId(R.id.button)).perform(click())

        onView(withId(R.id.recyclerView_countrydetails)).check(matches(isDisplayed()))
        onView(withId(R.id.textView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun verify_textOnActionBar_onSuccessResponse() {
        onView(withId(R.id.button)).perform(click())

        onView(
            allOf(
                instanceOf(TextView::class.java),
                withParent(withResourceName("action_bar"))
            )
        ).check(matches((withText(countrydetails.title))))
    }

    @Test
    fun verify_textOnActionBar_isNotEmpty() {
        onView(withId(R.id.button)).perform(click())

        onView(
            allOf(
                instanceOf(TextView::class.java),
                withParent(withResourceName("action_bar"))
            )
        ).check(matches(not(withText(isEmptyString()))))
    }

    @Test
    fun verify_listRowIsClicked() {
        onView(withId(R.id.button)).perform(click())

        onView(withId(R.id.recyclerView_countrydetails))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<CustomAdapter.CountryViewHolder>(
                        ITEM_POSITION_1,
                        click()
                    )
            )
    }

    @Test
    fun verify_listItemIsClicked() {
        onView(withId(R.id.button)).perform(click())

        onView(withId(R.id.recyclerView_countrydetails))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<CustomAdapter.CountryViewHolder>(
                        ITEM_POSITION_1,
                        clickItemWithId(R.id.txt_title)
                    )
            )
    }

    @Test
    fun verify_itemWithTitle_isDisplayed() {
        onView(withId(R.id.button)).perform(click())

        onView(withText(countrydetails.rows[ITEM_POSITION_FIRST].title)).check(matches(isDisplayed()))
    }

    @Test
    fun verify_itemWithDescription_isDisplayed() {
        onView(withId(R.id.button)).perform(click())

        onView(withText("Hockey Night in Canada")).check(matches(isDisplayed()))
    }

    @Test
    fun verify_itemWithDescription_doesNotExisxt() {
        onView(withId(R.id.button)).perform(click())

          onView(withId(R.id.recyclerView_countrydetails)).check(
              matches(
                  childOfViewAtPositionWithMatcher(
                      R.id.txt_details,
                      position = 1,
                      childMatcher = withText(countrydetails.rows[1].description)
                  )
              )
          )

       }

    @Test
    fun verify_listIsScrollableToLastPosition() {
        onView(withId(R.id.button)).perform(click())

        onView(withId(R.id.recyclerView_countrydetails)).perform(
            RecyclerViewActions.scrollToPosition<CustomAdapter.CountryViewHolder>(
                ITEM_POSITION_LAST
            )
        )
    }

    @Test
    fun verify_listItemAtPositionMatchesText() {
        onView(withId(R.id.button)).perform(click())

        onView(withId(R.id.recyclerView_countrydetails)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.txt_title,
                    position = 2,
                    childMatcher = withText(countrydetails.rows[2].title)
                )
            )
        )
    }

    /**
     * This method verifies data for recyclerview child items at a given position
     * @param childId recyclerview items id
     * @param position  position in recyclerview
     * @param childMatcher view to match with
     */
    private fun childOfViewAtPositionWithMatcher(
        childId: Int,
        position: Int,
        childMatcher: Matcher<View>
    ): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("Checks the matcher matches with a view having a given id inside Recyclerview")
            }

            override fun matchesSafely(recyclerView: RecyclerView?): Boolean {
                val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position)
                val matcher = hasDescendant(allOf(withId(childId), childMatcher))
                return viewHolder != null && matcher.matches(viewHolder.itemView)

            }

        }
    }

    /**
     * This method verifies click or action peformed on a recylerview or its child
     * @param id recyclerview items id
     */
    private fun clickItemWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById(id) as View
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
            ),
            Rows(
                title = "Flag",
                description = "",
                imageHref = "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg"
            ),
            Rows(
                title = "Transportation",
                description = "It is a well known fact that polar bears are the main mode of transportation in Canada. They consume far less gas and have the added benefit of being difficult to steal.",
                imageHref = "http://1.bp.blogspot.com/_VZVOmYVm68Q/SMkzZzkGXKI/AAAAAAAAADQ/U89miaCkcyo/s400/the_golden_compass_still.jpg"
            ),
            Rows(
                title = "Hockey Night in Canada",
                description = "These Saturday night CBC broadcasts originally aired on radio in 1931. In 1952 they debuted on television and continue to unite (and divide) the nation each week.",
                imageHref = "http://fyimusic.ca/wp-content/uploads/2008/06/hockey-night-in-canada.thumbnail.jpg"
            ),
            Rows(
                title = "Eh",
                description = "These Saturday night CBC broadcasts originally aired on radio in 1931. In 1952 they debuted on television and continue to unite (and divide) the nation each week.",
                imageHref = ""
            )
        )
    )


    companion object {
        private val ITEM_POSITION_FIRST = 0
        private val ITEM_POSITION_1 = 1
        private val ITEM_POSITION_LAST = 12
    }
}
