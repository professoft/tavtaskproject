package com.professoft.tavtask.base

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.professoft.tavtask.R
import com.professoft.tavtask.ui.components.LoginDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    private var baseViewModel: BaseViewModel? = null
    private var dialog: Dialog? = null
    private var builder: AlertDialog.Builder? = null
    private var loginDialog: LoginDialog? = null
    var activeUser: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLoadingCallback()
        baseViewModel?.let { initViewModel(it) }
    }

    fun initViewModel(viewModel: BaseViewModel) {
        baseViewModel = viewModel
        observeLoadingCallback()
    }

    private fun observeLoadingCallback() {
        baseViewModel?.loading?.observe {
            if(!it.equals(this.getString(R.string.loading_hide_message))) {
                showLoading(it)
            }
            else {
                hideLoading()
            }
        }
    }

    open fun showLoading(loading_message: String) {
        hideLoading()
        dialog = Dialog(this)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.setCancelable(false)
        val message= dialog?.findViewById<TextView>(R.id.loading_message)
        message?.text = loading_message
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
    }

    open fun hideLoading() {
        if (isDestroyed) return
        dialog?.dismiss()
    }

    fun <T> LiveData<T>.observe(block: (T) -> Unit) = observe(this@BaseActivity, Observer {
        block(it)
    })

    open fun showAlertMessage(networkError: String){
        hideLoading()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert))
        builder.setMessage(networkError)
        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->}
        dialog = builder.show()
    }
}

