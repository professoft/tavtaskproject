package com.professoft.tavtask.ui.currencyConverter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.R
import com.professoft.tavtask.adapters.CurrencyAdapter
import com.professoft.tavtask.databinding.FragmentCurrencyConverterBinding
import com.professoft.tavtask.utils.CurrencyConverterItemModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class CurrencyConverterFragment(var activeUser: Boolean) : Fragment() {
    private var _binding: FragmentCurrencyConverterBinding? = null
    lateinit var manager: RecyclerView.LayoutManager

    private val binding get() = _binding!!
    lateinit var currencyConverterList: List<CurrencyConverterItemModel>
    lateinit var currenyRatio : HashMap <String,BigDecimal>
    lateinit var arrayAdapter: ArrayAdapter<String>

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
        manager = LinearLayoutManager(context)
        var arrayCurrencies : Array<String> = resources.getStringArray(R.array.currencies)
        binding.convertButton.isEnabled = activeUser
        arrayAdapter = ArrayAdapter<String>(requireActivity(), R.layout.spinner_item, requireActivity().resources.getStringArray(R.array.currencies))
        binding.spinnerConvercies.adapter = arrayAdapter

        //binding.currencyConverterRecyclerView.adapter = currencyConverterAdapter
        viewModel.getLatestCurrencies(requireActivity())
        latestCurrenciesCallback(arrayCurrencies)
        binding.convertButton.setOnClickListener {
            if (nullCheck()) {
                var ratio = currenyRatio.get(arrayCurrencies.get(binding.spinnerConvercies.selectedItemPosition))
                var amount = binding.inputFields.text.toString().toBigDecimal()
                var result = (ratio!!.multiply(amount)).toString()
                binding.amountFields.setText(result)
            }
        }
    }

    private fun latestCurrenciesCallback(arrayCurrencies: Array<String>) {
        viewModel.currenciesList.observe(requireActivity()) {
                currencyConverterList =
                    MutableList(it.sar.size) { CurrencyConverterItemModel("", "", "") }
                for (position in 0..it.sar.size - 1) {
                    var currency_name: String = ArrayList<String>(it.sar.keys).get(position)
                    currencyConverterList.get(position).currencyType = currency_name
                    arrayCurrencies[position] = currency_name

                    currenyRatio = it.sar
                    var ratio: BigDecimal = ArrayList<BigDecimal>(it.sar.values).get(position)
                    currencyConverterList.get(position).ask = ratio.setScale(3, BigDecimal.ROUND_HALF_EVEN).toString()
                    currencyConverterList.get(position).bid = ratio.setScale(3, BigDecimal.ROUND_HALF_EVEN).toString()
                }
            arrayAdapter.notifyDataSetChanged()
            binding.currencyConverterRecyclerView.apply {
                adapter = CurrencyAdapter(currencyConverterList)
                layoutManager = manager
            }
            binding.currencyConverterRecyclerView.adapter!!.notifyDataSetChanged()
            //currencyConverterAdapter.setCurrencyList(currencyConverterList)
            }
    }
    private fun nullCheck() : Boolean{

        if(binding.inputFields.text.isNullOrEmpty()){
            binding.inputFields.error = context?.getString(R.string.enterAmount)
        }

        return  binding.inputFields.error.isNullOrEmpty()
    }
}