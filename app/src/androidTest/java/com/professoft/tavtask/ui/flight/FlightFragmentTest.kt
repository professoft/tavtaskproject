package com.professoft.tavtask.ui.flight

import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.professoft.tavtask.R
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest


@RunWith(AndroidJUnit4::class)
class FlightFragmentTest : AutoCloseKoinTest() {

    @Test
    fun buttons_Onclick_Enabled_Control() {
        launchFragment<FlightFragment>()
        onView(withId(R.id.arrivalButton)).perform((click()))
        onView(withId(R.id.arrivalButton)).check(ViewAssertions.matches(not(isEnabled())))
        onView(withId(R.id.departureButton)).check(ViewAssertions.matches(isEnabled()))
        onView(withId(R.id.departureButton)).perform((click()))
        onView(withId(R.id.departureButton)).check(ViewAssertions.matches(not(isEnabled())))
        onView(withId(R.id.arrivalButton)).check(ViewAssertions.matches(isEnabled()))
    }
}
