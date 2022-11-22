package com.professoft.tavtask.ui.main


import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.professoft.tavtask.R
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest



@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class MainActivityTest : AutoCloseKoinTest() {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private val mainViewModel: MainViewModel = mockk(relaxed = true)

    @Before
    fun setUp() {
        GlobalContext.startKoin {
            modules(
                module { mainViewModel }
            )
        }
        scenario.moveToState(Lifecycle.State.INITIALIZED)

    }
    @After
    fun move() {
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.moveToState(Lifecycle.State.RESUMED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    fun flight_Fragment_Button_Onclick_Enabled_Control() {
        onView(withId(R.id.flightsButton)).perform((click()))
        onView(withId(R.id.flightsButton)).check(ViewAssertions.matches(not(isEnabled())))
        onView(withId(R.id.currencyConverterButton)).check(ViewAssertions.matches(isEnabled()))
        onView(withId(R.id.convertButton)).perform((click()))
        onView(withId(R.id.convertButton)).check(ViewAssertions.matches(not(isEnabled())))
        onView(withId(R.id.flightsButton)).check(ViewAssertions.matches(isEnabled()))
    }
}
