package com.professoft.tavtask.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.TAG
import androidx.fragment.app.FragmentTransaction
import com.professoft.tavtask.R
import com.professoft.tavtask.base.BaseActivity
import com.professoft.tavtask.ui.components.LoginDialog
import com.professoft.tavtask.ui.currencyConverter.CurrencyConverterFragment
import com.professoft.tavtask.ui.flight.FlightFragment
import androidx.core.os.BuildCompat
import com.professoft.tavtask.databinding.ActivityMainBinding
import com.professoft.tavtask.utils.Keys

@BuildCompat.PrereleaseSdkCheck
class MainActivity : BaseActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var flightsButton: Button
    private lateinit var currencyConverterButton: Button
    private lateinit var loginButton: ImageView
    private var activeUser: Boolean = false
    private var loginDialog : LoginDialog? = null
    private var checkActiveUserForCurrencyConverter : Boolean = false
    private var fragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        onNewBackPressed()
    }

    private fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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

    private fun checkLoginCallback() {
        viewModel.checkLogin.observe(this) {
            when (it) {
                false -> {
                    showAlertDialog(false)
                }
                true -> {

                    loginDialog?.let { dialog ->
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                    }
                    checkActiveUserForCurrencyConverter = false
                    activeUser = true
                    loginButton.setImageResource(R.drawable.profile_information)
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun checkDefaultUserCallback() {
        viewModel.checkRegistration.observe(this) {
            when (it) {
                false -> {
                    viewModel.registrationDefaultUser("user@test.com", "1234")
                }

                true -> {
                    Log.d(TAG, "Default user creation successful")
                }
            }
        }
    }

    private fun checkActiveUserCallback() {
        viewModel.checkActiveUser.observe(this) {
            when (it) {
                false -> {
                    if (checkActiveUserForCurrencyConverter) {
                        showAlertDialog(true)
                    }
                }

               true -> {
                    activeUser = true
                   loginButton.setImageResource(R.drawable.profile_information)
                }
            }
        }
    }

    private fun showAlertDialog(must: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert))
        if(must){
            builder.setMessage(getString(R.string.must_login))
        }
        else{
            builder.setMessage(getString(R.string.alertLogin))
        }
        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            if(must) {
                loginDialog = LoginDialog(this) { mail, password ->
                    viewModel.checkUser(mail, password)
                }
                loginDialog?.show()
            }
        }
        builder.show()
    }

    private fun loginOrProfileInformation() {

        if (!activeUser) {
            loginDialog = LoginDialog(this) { mail, password ->
                viewModel.checkUser(mail, password)
            }
            loginDialog?.show()
        }
        // else profile information
    }

    private fun navigateFlights() {
        changeButtonStyle(flightsButton, currencyConverterButton)
        checkActiveUserForCurrencyConverter = false
        selectFragment(FlightFragment())
    }

    private fun navigateCurrencyConverter() {
        changeButtonStyle(currencyConverterButton, flightsButton)
        checkActiveUserForCurrencyConverter = true
        viewModel.checkActiveUser()
        checkLoginCallback()
        selectFragment(CurrencyConverterFragment(activeUser))

    }

    private fun changeButtonStyle(selected: Button, default: Button) {
        selected.background = ContextCompat.getDrawable(this, R.drawable.selected_button_background);
        default.background = ContextCompat.getDrawable(this, R.drawable.default_button_background);
        selected.setTextColor(ContextCompat.getColor(this, R.color.color_tab))
        default.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun selectFragment(Fragment: Fragment){
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
        }
        else {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                        finish()
                }
            })
        }
    }
}