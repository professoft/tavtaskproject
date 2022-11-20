package com.professoft.tavtask.ui.currencyConverter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.ads.mediationtestsuite.viewmodels.ViewModelFactory
import com.professoft.tavtask.R
import com.professoft.tavtask.adapters.CurrencyConverterAdapter
import com.professoft.tavtask.databinding.FragmentCurrencyConverterBinding
import com.professoft.tavtask.utils.CurrencyConverterItemModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class CurrencyConverterFragment(var activeUser: Boolean) : Fragment() {
    private var _binding: FragmentCurrencyConverterBinding? = null
    private val binding get() = _binding!!
    val currencyConverterAdapter = CurrencyConverterAdapter()
    lateinit var currencyConverterList: List<CurrencyConverterItemModel>
    lateinit var currenyRatio : HashMap <String,BigDecimal>


    companion object {
        fun newInstance() = CurrencyConverterFragment(false)
    }

    private lateinit var viewModel: CurrencyConverterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[CurrencyConverterViewModel::class.java]
        var arrayCurrencies : Array<String> = resources.getStringArray(R.array.currencies)
        binding.convertButton.isEnabled = activeUser
        val arrayAdapter = ArrayAdapter<String>(requireActivity(), R.layout.spinner_item, requireActivity().resources.getStringArray(R.array.currencies))
        binding.spinnerConvercies.adapter = arrayAdapter

        /*currencyConverterList = MutableList(1){ CurrencyConverterItemModel("","","") }
        currencyConverterAdapter= CurrencyConverterAdapter(currencyConverterList)*/
        binding.currencyConverterRecyclerView.adapter = currencyConverterAdapter
        viewModel.getLatestCurrencies(requireActivity())
        latestCurrenciesCallback(arrayCurrencies)
        binding.convertButton.setOnClickListener {
            if (nullCheck()) {
                var ratio: BigDecimal? = currenyRatio.get(arrayCurrencies.get(binding.spinnerConvercies.selectedItemPosition))
                var amount: BigDecimal? = binding.inputFields.text.toString().toBigDecimal()
                var result: String? = (ratio!!.multiply(amount)).toString()
                binding.amountFields.setText(result)
            }
        }
    }

    private fun latestCurrenciesCallback(arrayCurrencies: Array<String>) {
        viewModel.currenciesList.observe(requireActivity()) {
                currencyConverterList =
                    MutableList(it.sar.size) { CurrencyConverterItemModel("", "", "") }
                for (position in 0..it.sar.size - 1) {
                    currencyConverterList.get(position).currencyType = arrayCurrencies[position]
                    currenyRatio = it.sar
                    var ask: BigDecimal = ArrayList<BigDecimal>(it.sar.values).get(position)
                    currencyConverterList.get(position).ask = ask.toString()
                    currencyConverterList.get(position).bid = ask.toString()
                }
                    currencyConverterAdapter.setCurrencyList(currencyConverterList)
            }
    }
    private fun nullCheck() : Boolean{

        if(binding.amountFields.text.isNullOrEmpty()){
            binding.amountFields.error = context?.getString(R.string.enterPassword)
        }

        return  binding.amountFields.error.isNullOrEmpty()
    }
}