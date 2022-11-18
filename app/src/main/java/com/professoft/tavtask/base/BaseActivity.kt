package com.professoft.tavtask.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
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

    }

    fun initViewModel(viewModel: BaseViewModel) {
        baseViewModel = viewModel
        baseViewModel?.viewModelScope?.launch {
            observeLoadingCallback()
        }
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

    private fun showLoading() {
        hideLoading()
        dialog = Dialog(this)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.setCancelable(false)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.show()
    }

    private fun hideLoading() {
        if (isDestroyed) return
        dialog?.dismiss()
    }

}

private fun <T> MutableLiveData<T>?.observe(function: (T) -> Unit) {

}
