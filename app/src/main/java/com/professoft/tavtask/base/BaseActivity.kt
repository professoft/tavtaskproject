package com.professoft.tavtask.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.professoft.tavtask.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    private var baseViewModel: BaseViewModel? = null
    private var dialog: Dialog? = null

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
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }

        }
    }

    open fun showLoading() {
        hideLoading()
        dialog = Dialog(this)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.setCancelable(false)
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
        Toast.makeText(this,networkError,Toast.LENGTH_SHORT)
    }
}

