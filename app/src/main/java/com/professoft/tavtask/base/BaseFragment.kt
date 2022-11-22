package com.professoft.tavtask.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.professoft.tavtask.R

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var baseViewModel: BaseViewModel? = null
    private var baseActivity: BaseActivity? = null
    private var dialog: Dialog? = null
    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoadingCallback()
        observeNetworkErrorCallback()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater,container)
        return binding.root
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    fun initViewModel(viewModel: BaseViewModel) {
        baseViewModel = viewModel
        observeLoadingCallback()
        observeNetworkErrorCallback()
    }

    fun observeLoadingCallback() {
        baseViewModel?.loading?.observe(viewLifecycleOwner) {
                if(it != requireContext().getString(R.string.loading_hide_message)) {
                    baseActivity?.showLoading(it)
                }
                else {
                    baseActivity?.hideLoading()
                }
            }
    }
    fun observeNetworkErrorCallback(){
        baseViewModel?.networkError?.observe(viewLifecycleOwner) {
                baseActivity?.showAlertMessage(baseViewModel?.networkError!!.value.toString())
        }
    }
}

