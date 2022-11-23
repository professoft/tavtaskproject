package com.professoft.tavtask.ui.currencyConverter

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.professoft.tavtask.R
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class CurrencyConverterFragmentTest : AutoCloseKoinTest() {

    private lateinit var scenario: FragmentScenario<CurrencyConverterFragment>
    private val currencyConverterViewModel: CurrencyConverterViewModel = mockk(relaxed = true)

    @Before
    fun setUp() {
        startKoin {
            modules(
                module { currencyConverterViewModel }
            )
        }
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_TavTaskProject)
        scenario.moveToState(newState = Lifecycle.State.CREATED)
    }

    @Test
    fun convert_Button_OnClick_Enabled_Control() {
        onView(withId(R.id.convertButton)).check(ViewAssertions.matches(not(isEnabled())))
    }
}
