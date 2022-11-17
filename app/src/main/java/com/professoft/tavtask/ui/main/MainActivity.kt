package com.professoft.tavtask.ui.main

import android.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.professoft.tavtask.R
import com.professoft.tavtask.base.BaseActivity
import com.professoft.tavtask.databinding.ActivityMainBinding
import com.professoft.tavtask.ui.components.LoginDialog
import com.professoft.tavtask.ui.currencyConverter.CurrencyConverterFragment
import com.professoft.tavtask.ui.flight.FlightFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var flightsButton: Button
    private lateinit var currencyConverterButton: Button
    private lateinit var loginButton: ImageView
    private var activeUser: Boolean = false
    private var loginDialog = LoginDialog()
    private var checkActiveUserForCurrencyConverter : Boolean = false
    private var fragment: Fragment? = null

    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        selectFragment(FlightFragment())
        flightsButton = binding.flightsButton
        currencyConverterButton = binding.currencyConverterButton
        loginButton = binding.loginButton
        viewModel.checkDefaultUser()
        viewModel.checkActiveUser()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    checkDefaultUserCallback()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    checkActiveUserCallback()
                }
            }
        }
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

    private suspend fun checkDefaultUserCallback() {
        viewModel.viewState4Login.collect {
            when (it) {
                MainViewModel.UserLoginSealed.ERROR -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    viewModel.registrationDefaultUser("user@test.com", "1234")
                }
                MainViewModel.UserLoginSealed.IDLE -> binding.progressBar.visibility =
                    View.INVISIBLE
                MainViewModel.UserLoginSealed.LOADING -> binding.progressBar.visibility =
                    View.VISIBLE
                MainViewModel.UserLoginSealed.SUCCESS -> binding.progressBar.visibility =
                    View.INVISIBLE
            }
        }
    }

    private suspend fun checkActiveUserCallback() {
        viewModel.viewState4ActiveUser.collect {
            when (it) {
                MainViewModel.UserActiveSealed.ERROR -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    if (!checkActiveUserForCurrencyConverter) {
                    showAlertDialog()
                }
                    else{
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.must_login), Toast.LENGTH_SHORT
                        ).show()
                        loginDialog.apply {
                            this.listener = viewModel
                        }.show(supportFragmentManager, getString(R.string.loginDialogTag))
                    }
                }
                MainViewModel.UserActiveSealed.IDLE -> binding.progressBar.visibility =
                    View.INVISIBLE
                MainViewModel.UserActiveSealed.LOADING -> binding.progressBar.visibility =
                    View.VISIBLE
                MainViewModel.UserActiveSealed.SUCCESS -> {
                        loginDialog.checkAndDismiss()

                    binding.progressBar.visibility = View.INVISIBLE
                    activeUser = true
                    loginButton.background =
                        ContextCompat.getDrawable(this, R.drawable.profile_information);
                }
            }
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert))
        builder.setMessage(getString(R.string.alertLogin))

        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            Toast.makeText(
                applicationContext,
                getString(R.string.ok), Toast.LENGTH_SHORT
            ).show()
        }
        builder.show()
    }

    private fun loginOrProfileInformation() {

        if (!activeUser) {
            loginDialog.apply {
                this.listener = viewModel
            }.show(supportFragmentManager, getString(R.string.loginDialogTag))
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
        selectFragment(CurrencyConverterFragment())
    }

    private fun changeButtonStyle(selected: Button, default: Button) {
        selected.background =
            ContextCompat.getDrawable(this, R.drawable.selected_button_background);
        default.background = ContextCompat.getDrawable(this, R.drawable.default_button_background);
        selected.setTextColor(ContextCompat.getColor(this, R.color.color_tab))
        default.setTextColor(ContextCompat.getColor(this, R.color.white))
    }
    fun selectFragment(Fragment: Fragment){
        fragment = Fragment
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frameLayout, fragment as Fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }
}