package com.professoft.tavtask.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.professoft.tavtask.R
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var baseViewModel: BaseViewModel? = null
    private var dialog: Dialog? = null
    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    abstract fun getViewBinding(): VB

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
        dialog = Dialog(requireContext())
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.setCancelable(false)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.show()
    }

    private fun hideLoading() {
        if (context == null) return
        dialog?.dismiss()
    }

}

private fun <T> MutableLiveData<T>?.observe(function: (T) -> Unit) {

}