package com.professoft.tavtask.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.professoft.tavtask.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    private var baseViewModel: BaseViewModel? = null
    private var dialog: Dialog? = null
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
            if (!it.equals(this.getString(R.string.loading_hide_message))) {
                showLoading(it)
            } else {
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
        val message = dialog?.findViewById<TextView>(R.id.messageTextView)
        message?.text = loading_message
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
    }

    open fun showWarning(warning_message: String) {
        hideLoading()
        dialog = Dialog(this)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_error)
        dialog?.setCancelable(false)
        val message = dialog?.findViewById<TextView>(R.id.errorTextView)
        message?.text = warning_message
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
        Handler(Looper.getMainLooper()).postDelayed({
            dialog?.cancel()
        }, 1500)
    }

    open fun hideLoading() {
        if (isDestroyed) return
        dialog?.dismiss()
    }

    fun <T> LiveData<T>.observe(block: (T) -> Unit) = observe(this@BaseActivity, Observer {
        block(it)
    })

}

