package com.professoft.tavtask.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.os.BuildCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.professoft.point.data.EventBus.CustomMessageEvent
import com.professoft.tavtask.R
import com.professoft.tavtask.base.BaseActivity
import com.professoft.tavtask.databinding.ActivityMainBinding
import com.professoft.tavtask.ui.components.LoginDialog
import com.professoft.tavtask.ui.currencyConverter.CurrencyConverterFragment
import com.professoft.tavtask.ui.flight.FlightFragment
import dagger.hilt.android.AndroidEntryPoint

@BuildCompat.PrereleaseSdkCheck
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var flightsButton: Button
    private lateinit var currencyConverterButton: Button
    private lateinit var loginButton: ImageView
    private var loginDialog: LoginDialog? = null
    private var checkActiveUserForCurrencyConverter: Boolean = false
    private var fragment: Fragment? = null
    var loginEvent = MutableLiveData<CustomMessageEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        onNewBackPressed()
    }

    private fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initViewModel(viewModel)
        if(!viewModel.isOnline(this)){
            showWarning(getString(R.string.warning_no_internet_connection))
        }
        selectFragment(FlightFragment())
        flightsButton = binding.flightsButton
        currencyConverterButton = binding.currencyConverterButton
        loginButton = binding.loginButton
        viewModel.checkDefaultUser()
        viewModel.checkActiveUser()
        checkDefaultUserCallback()
        checkActiveUserCallback()
        checkLoginCallback()
        flightsButton.setOnClickListener {
            navigateFlights()
        }
        currencyConverterButton.setOnClickListener {
            navigateCurrencyConverter()
        }
        loginButton.setOnClickListener {
            loginOrProfileInformation()

        }
    }



    private fun checkDefaultUserCallback() {
        viewModel.checkRegistration.observe(this) {
            if(!it) {
                    viewModel.registrationDefaultUser("user@test.com", "1234")
            }
        }
    }

    private fun checkActiveUserCallback() {
        viewModel.checkActiveUser.observe(this) {
            when (it) {
                false -> {
                    if (checkActiveUserForCurrencyConverter) {
                        showWarning(getString(R.string.warning_login_must))
                        Handler(Looper.getMainLooper()).postDelayed({
                            loginDialog = LoginDialog(this) { mail, password ->
                                viewModel.checkUser(mail, password)
                            }
                            loginDialog?.show()
                        }, 1500)
                    }
                }
                true -> {
                    activeUser = true
                    loginButton.setImageResource(R.drawable.profile_information)
                    if (checkActiveUserForCurrencyConverter) {
                        selectFragment(CurrencyConverterFragment(activeUser))
                    }
                }
            }
        }
    }

    private fun checkLoginCallback() {
        viewModel.checkLogin.observe(this) {
            when (it) {
                false -> {
                    showWarning(getString(R.string.warning_incorrect_login))
                }
                true -> {
                    loginDialog?.let { dialog ->
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                    }
                    activeUser = true
                    val event = CustomMessageEvent("login")

                    if (loginEvent.hasActiveObservers()) {
                        loginEvent.postValue(event)
                    }
                }
            }
        }
    }

    private fun loginOrProfileInformation() {
        if (!activeUser) {
            loginDialog = LoginDialog(this) { mail, password ->
                viewModel.checkUser(mail, password)
            }
            loginDialog?.show()
        }
    }

    private fun navigateFlights() {
        if(!viewModel.isOnline(this)){
            showWarning(getString(R.string.warning_no_internet_connection))
        }
        flightsButton.isEnabled = false
        currencyConverterButton.isEnabled = true
        changeButtonStyle(flightsButton, currencyConverterButton)
        checkActiveUserForCurrencyConverter = false
        selectFragment(FlightFragment())
    }

    private fun navigateCurrencyConverter() {
        if(!viewModel.isOnline(this)){
            showWarning(getString(R.string.warning_no_internet_connection))
        }
        currencyConverterButton.isEnabled = false
        flightsButton.isEnabled = true
        changeButtonStyle(currencyConverterButton, flightsButton)
        checkActiveUserForCurrencyConverter = true
        viewModel.checkActiveUser()
        checkLoginCallback()
        selectFragment(CurrencyConverterFragment(activeUser))

    }

    private fun changeButtonStyle(selected: Button, default: Button) {
        selected.background =
            ContextCompat.getDrawable(this, R.drawable.selected_button_background)
        default.background = ContextCompat.getDrawable(this, R.drawable.default_button_background)
        selected.setTextColor(ContextCompat.getColor(this, R.color.color_tab))
        default.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun selectFragment(Fragment: Fragment) {
        fragment = Fragment
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frameLayout, fragment as Fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    private fun onNewBackPressed() {
        if (BuildCompat.isAtLeastT()) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                finish()
            }
        } else {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            })
        }
    }
}