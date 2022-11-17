package com.professoft.tavtask.ui.currencyConverter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.adapters.CurrencyConverterAdapter
import com.professoft.tavtask.databinding.FragmentCurrencyConverterBinding
import com.professoft.tavtask.utils.CurrencyConverterItemsModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyConverterFragment : Fragment() {
    private var _binding: FragmentCurrencyConverterBinding? = null
    private val binding get() = _binding!!
    lateinit var currencyConverterRecyclerView: RecyclerView
    lateinit var currencyConverterAdapter: CurrencyConverterAdapter
    lateinit var currenyConverterList: ArrayList<CurrencyConverterItemsModel>
    companion object {
        fun newInstance() = CurrencyConverterFragment()
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

}