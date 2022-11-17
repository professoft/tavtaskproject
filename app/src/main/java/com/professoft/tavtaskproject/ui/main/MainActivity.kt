package com.professoft.tavtaskproject.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.professoft.tavtaskproject.R
import com.professoft.tavtaskproject.databinding.ActivityMainBinding
import com.professoft.tavtaskproject.base.BaseActivity
import com.professoft.tavtaskproject.ui.components.LoginDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var flightsButton : Button
    private lateinit var currencyConverterButton : Button
    private lateinit var loginButton : Button
    private var activeUser : Boolean = false
    private lateinit var loginDialog : LoginDialog

    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        flightsButton = binding.flightsButton
        currencyConverterButton = binding.currencyConverterButton
        loginButton = binding.loginButton
        viewModel.checkDefaultUser()
        viewModel.checkActiveUser()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    checkDefaultUserCallback()
                    checkActiveUserCallback()
                }
            }
        }
        flightsButton.setOnClickListener{
            navigateFlights()
        }
        currencyConverterButton.setOnClickListener{
            navigateCurrencyConverter()
        }
        loginButton.setOnClickListener{
            loginOrProfileInformation()
        }
    }

    private suspend fun checkDefaultUserCallback(){
        viewModel.viewState4Login.collect{
            when(it){
                MainViewModel.UserLoginSealed.ERROR -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    viewModel.registrationDefaultUser("user@test.com","1234")
                }
                MainViewModel.UserLoginSealed.IDLE -> binding.progressBar.visibility = View.INVISIBLE
                MainViewModel.UserLoginSealed.LOADING -> binding.progressBar.visibility = View.VISIBLE
                MainViewModel.UserLoginSealed.SUCCESS -> binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private suspend fun checkActiveUserCallback(){
        viewModel.viewState4ActiveUser.collect{
            when(it){
                MainViewModel.UserActiveSealed.ERROR -> {binding.progressBar.visibility = View.INVISIBLE
                    showAlertDialog()
                }
                MainViewModel.UserActiveSealed.IDLE -> binding.progressBar.visibility = View.INVISIBLE
                MainViewModel.UserActiveSealed.LOADING -> binding.progressBar.visibility = View.VISIBLE
                MainViewModel.UserActiveSealed.SUCCESS -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    activeUser = true
                    loginButton.background = ContextCompat.getDrawable(this, R.drawable.profile_information);
                }
            }
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage(getString(R.string.alertLogin))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    private fun loginOrProfileInformation(){

        if(!activeUser){
            LoginDialog().apply {
                this.listener = viewModel
            }.show(supportFragmentManager, "loginDialog")
        }
        // else profile information
    }

    private fun navigateFlights(){
        changeButtonStyle(flightsButton,currencyConverterButton)
    }

    private fun navigateCurrencyConverter(){
        changeButtonStyle(currencyConverterButton,flightsButton)
    }
    private fun changeButtonStyle(selected : Button, default : Button){
        selected.background = ContextCompat.getDrawable(this, R.drawable.selected_button_background);
        default.background = ContextCompat.getDrawable(this, R.drawable.default_button_background);
        selected.setTextColor(ContextCompat.getColor(this, R.color.color_tab))
        default.setTextColor(ContextCompat.getColor(this, R.color.white))
    }
}