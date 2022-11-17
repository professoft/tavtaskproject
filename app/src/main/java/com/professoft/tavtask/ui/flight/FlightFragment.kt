package com.professoft.tavtask.ui.flight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.adapters.FlightsAdapter
import com.professoft.tavtask.databinding.FragmentFlightBinding
import com.professoft.tavtask.utils.FlightItemsModel
import java.util.*
import kotlin.collections.ArrayList

class FlightFragment : Fragment() {
    private var _binding: FragmentFlightBinding? = null
    private val binding get() = _binding!!
    lateinit var flightRecyclerView: RecyclerView
    lateinit var flightsAdapter: FlightsAdapter
    lateinit var flightsList: ArrayList<FlightItemsModel>
    companion object {
        fun newInstance() = FlightFragment()
    }

    private lateinit var viewModel: FlightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FlightViewModel::class.java)

        flightRecyclerView = binding.flightsRecyclerView
        flightsList = ArrayList()
        flightsAdapter = FlightsAdapter(flightsList)
        flightRecyclerView.adapter = flightsAdapter

        binding.departureButton.setOnClickListener{
            // ToDo:
        }

        binding.arrivalButton.setOnClickListener{
            // ToDo:
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(filter: String): Boolean {
                filter(filter)
                return false
            }
        })
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<FlightItemsModel> = ArrayList()

        for (item in flightsList) {
            if (item.destination.uppercase(Locale.getDefault())
                    .contains(text.uppercase(Locale.getDefault()))) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(activity, "Flight Not Found..", Toast.LENGTH_SHORT).show()
        } else {
            flightsAdapter.filterList(filteredList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlightBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}