package com.professoft.tavtask.ui.flight

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.R
import com.professoft.tavtask.adapters.FlightsAdapter
import com.professoft.tavtask.base.BaseFragment
import com.professoft.tavtask.databinding.FragmentFlightBinding
import com.professoft.tavtask.models.FlightDataModel
import com.professoft.tavtask.models.FlightResponseModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.io.InputStream
import java.util.*


@AndroidEntryPoint
class FlightFragment : BaseFragment<FragmentFlightBinding>() {

    private lateinit var binding: FragmentFlightBinding
    private lateinit var flightRecyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var flightsAdapter: FlightsAdapter
    private lateinit var departureButton: Button
    private lateinit var arrivalButton: Button
    private lateinit var flightsList: FlightResponseModel
    private var isDeparture = true
    private var isLoading = false

    companion object {
    }

    private lateinit var viewModel: FlightViewModel


    private fun filter(text: String) {
        val filteredList: ArrayList<FlightDataModel> = ArrayList()

        for ((position, item) in flightsList.data.withIndex()) {
            if (isDeparture) {
                if (item.departure.airport.uppercase(Locale.getDefault())
                        .contains(text.uppercase(Locale.getDefault()))
                ) {
                    filteredList.add(flightsList.data[position])
                }
            } else {
                if (item.arrival.airport.uppercase(Locale.getDefault())
                        .contains(text.uppercase(Locale.getDefault()))
                ) {
                    filteredList.add(flightsList.data[position])
                }
            }
        }
        if (filteredList.isEmpty()) {
            showWarning(getString(R.string.warning_search))
        } else {
            binding.flightsRecyclerView.adapter = FlightsAdapter(filteredList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentFlightBinding.inflate(LayoutInflater.from(layoutInflater.context))
        viewModel = ViewModelProvider(this)[FlightViewModel::class.java]
        initViewModel(viewModel)
        manager = LinearLayoutManager(context)
        departureButton = binding.departureButton
        arrivalButton = binding.arrivalButton
        flightRecyclerView = binding.flightsRecyclerView
        observeLoadingCallback()
        observeNetworkErrorCallback()
        updateFlightsCallback()
        initScrollListener()

        departureButton.setOnClickListener {
            if(!viewModel.isOnline(requireActivity())){
                showWarning(getString(R.string.warning_no_internet_connection))
            }
            else{
                viewModel.getDepartureFlights(requireActivity(), getString(R.string.icao))
            }
            viewModel.setOffset("0")
            isDeparture = true
            changeButtonStyle(departureButton, arrivalButton)
        }

        arrivalButton.setOnClickListener {
            if(!viewModel.isOnline(requireActivity())){
                showWarning(getString(R.string.warning_no_internet_connection))
            }
            else{
                viewModel.getArrivalFlights(requireActivity(), getString(R.string.icao))
            }
            viewModel.setOffset("0")
            isDeparture = false
            changeButtonStyle(arrivalButton, departureButton)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(search: String?): Boolean {
                if (search != null) {
                    filter(search)
                }
                else{
                    showWarning(getString(R.string.enterText))
                }
                return false
            }

            override fun onQueryTextChange(filter: String): Boolean {
                filter(filter)
                return false
            }
        })
        if(viewModel.isOnline(requireActivity())){
            viewModel.loading.postValue(requireContext().getString(R.string.flight_loading_message))
        }
        viewModel.getDepartureFlights(requireActivity(), getString(R.string.icao))
        isDeparture = true
        return binding.root
    }

    private fun updateFlightsCallback() {
        viewModel.flightList.observe(requireActivity()) { it ->
            if (it != null) {
                flightsList = it
                var position = 0
                flightsList.data.forEach { data ->
                    if (data.arrival.gate.isNullOrEmpty()) {
                        flightsList.data[position].arrival.gate = getString(R.string.unspecified)
                    }
                    if (data.departure.gate.isNullOrEmpty()) {
                        flightsList.data[position].departure.gate = getString(R.string.unspecified)
                    }
                    position++
                }
                position = 0
                flightsList.data.forEach {
                    flightsList.data[position].airline.airlineNameWithIcon =
                        iconMatching(it.airline.name)
                    flightsList.data[position].flight.isDeparture = isDeparture
                    position++
                }
                flightsAdapter = FlightsAdapter(flightsList.data)
                binding.flightsRecyclerView.apply {
                    adapter = FlightsAdapter(flightsList.data)
                    layoutManager = manager
                }
                binding.flightsRecyclerView.adapter!!.notifyDataSetChanged()
                val offset = viewModel.getOffset().toInt()
                viewModel.setOffset((offset + 10).toString())
            }
        }
    }

    private fun iconMatching(flightDate: String): Drawable {
        if (flightDate.contains("Turkish")) {
            return getAsset("turkish_airlines_icon.png")
        }
        if (flightDate.contains("Pegasus")) {
            return getAsset("pegasus_airlines_icon.png")
        }
        if (flightDate.contains("Azerbaijan")) {
            return getAsset("azerbaijan_airlines_icon.png")
        }
        if (flightDate.contains("Aegan")) {
            return getAsset("aegean_airlines_icon.png")
        }
        if (flightDate.contains("Freebird")) {
            return getAsset("freebird_airlines_icon.png")
        }
        if (flightDate.contains("Qatar")) {
            return getAsset("qatar_airways_icon.png")
        }
        if (flightDate.contains("Sunexpress")) {
            return getAsset("sunexpress_icon.png")
        }
        return ContextCompat.getDrawable(requireActivity(),R.drawable.tav_airports_international)!!
    }

    private fun getAsset(s: String): Drawable {
        return try {
            val ims: InputStream? = context?.assets?.open(s)
            val d = Drawable.createFromStream(ims, null)
            d!!
        } catch (ex: IOException) {
            AppCompatResources.getDrawable(requireActivity(),R.drawable.tav_airports_international)!!
        }
    }

    private fun changeButtonStyle(selected: Button, default: Button) {
        selected.isEnabled = false
        default.isEnabled = true
        selected.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selected_button_background)
        default.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_button_background)
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_tab))
        default.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }


    private fun initScrollListener() {
        flightRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == flightsList.data.size - 1) {
                        viewModel.loading.postValue(requireContext().getString(R.string.flight_updating_message))
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (isDeparture) {
                viewModel.getDepartureFlights(requireActivity(), getString(R.string.icao))
            } else {
                viewModel.getArrivalFlights(requireActivity(), getString(R.string.icao))
            }
            flightsAdapter.notifyDataSetChanged()
            isLoading = false
        }, 2000)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFlightBinding {
        return FragmentFlightBinding.inflate(inflater, container, false)
    }
}


