package com.professoft.tavtaskproject.ui.currencyConverter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.professoft.tavtaskproject.R

class CurrencyConverterFragment : Fragment() {

    companion object {
        fun newInstance() = CurrencyConverterFragment()
    }

    private lateinit var viewModel: CurrencyConverterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_converter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrencyConverterViewModel::class.java)
        // TODO: Use the ViewModel
    }

}