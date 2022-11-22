package com.professoft.tavtask.ui.currencyConverter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.R
import com.professoft.tavtask.adapters.CurrencyAdapter
import com.professoft.tavtask.base.BaseFragment
import com.professoft.tavtask.databinding.FragmentCurrencyConverterBinding
import com.professoft.tavtask.helpers.IntentService
import com.professoft.tavtask.models.CurrencyConverterItemModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal


@AndroidEntryPoint
class CurrencyConverterFragment(var activeUser: Boolean) :
    BaseFragment<FragmentCurrencyConverterBinding>() {
    lateinit var manager: RecyclerView.LayoutManager
    lateinit var currencyConverterList: List<CurrencyConverterItemModel>
    private lateinit var currencyRatio: HashMap<String, BigDecimal>
    lateinit var arrayAdapter: ArrayAdapter<String>

    companion object {
        fun newInstance() = CurrencyConverterFragment(false)
    }

    private lateinit var viewModel: CurrencyConverterViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[CurrencyConverterViewModel::class.java]
        initViewModel(viewModel)
        manager = LinearLayoutManager(context)
        var arrayCurrencies: Array<String> = resources.getStringArray(R.array.currencies)
        binding.convertButton.isEnabled = activeUser
        arrayAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            requireActivity().resources.getStringArray(R.array.currencies)
        )
        binding.spinnerConvercies.adapter = arrayAdapter
        binding.spinnerConvercies.setSelection(arrayCurrencies.indexOf("USD"))
        viewModel.getLatestCurrencies(requireActivity())
        latestCurrenciesCallback(arrayCurrencies)
        loginCallBack()
        binding.convertButton.setOnClickListener {
            if (nullCheck()) {
                dismissKeyboard(requireActivity())
                var ratio =currencyRatio.get(arrayCurrencies.get(binding.spinnerConvercies.selectedItemPosition))!!
                var input =binding.inputFields.text.toString().toBigDecimal()
                var result = viewModel.convertCurrency(ratio,input)
                binding.amountFields.setText(result.toString())
            }
        }
        eventBusListening()
    }

    private fun eventBusListening() {
        IntentService.loginEvent.observe(
            requireActivity(),
            Observer { event ->
                binding.convertButton.isEnabled = true
            })
    }

    private fun latestCurrenciesCallback(arrayCurrencies: Array<String>) {
        viewModel.currenciesList.observe(requireActivity()) {
            currencyConverterList =
                MutableList(it.sar.size) { CurrencyConverterItemModel("", "", "") }
            for (position in 0 until it.sar.size) {
                var currencyName: String = ArrayList<String>(it.sar.keys)[position]
                currencyConverterList[position].currencyType = currencyName
                arrayCurrencies[position] = currencyName

                currencyRatio = it.sar
                var ratio: BigDecimal = ArrayList<BigDecimal>(it.sar.values)[position]
                currencyConverterList[position].ask = ratio.toString()
                currencyConverterList[position].bid = ratio.toString()
            }
            arrayAdapter.notifyDataSetChanged()
            binding.currencyConverterRecyclerView.apply {
                adapter = CurrencyAdapter(currencyConverterList)
                layoutManager = manager
            }
            binding.currencyConverterRecyclerView.adapter!!.notifyDataSetChanged()
        }
    }
    private fun loginCallBack() {
        viewModel.login.observe(requireActivity()) {
                binding.convertButton.isEnabled = it
        }
    }

    private fun nullCheck(): Boolean {

        if (binding.inputFields.text.isNullOrEmpty()) {
            binding.inputFields.error = context?.getString(R.string.enterAmount)
        }

        return binding.inputFields.error.isNullOrEmpty()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCurrencyConverterBinding {
        return FragmentCurrencyConverterBinding.inflate(inflater, container, false)
    }
    private fun dismissKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus) imm.hideSoftInputFromWindow(
            activity.currentFocus!!
                .applicationWindowToken, 0
        )
    }
}