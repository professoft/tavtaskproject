package com.professoft.tavtask.ui.flight

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.R
import com.professoft.tavtask.adapters.FlightsAdapter
import com.professoft.tavtask.databinding.FragmentFlightBinding
import com.professoft.tavtask.utils.FlightsResponse
import java.io.IOException
import java.io.InputStream
import java.util.*


class FlightFragment : Fragment() {
    private lateinit var binding: FragmentFlightBinding
    lateinit var flightRecyclerView: RecyclerView
    lateinit var flightsAdapter: FlightsAdapter
    private lateinit var departureButton: Button
    private lateinit var arrivalButton: Button
    lateinit var flightsList: List<FlightsResponse>
    private var isDeparture = true;
    companion object {
        fun newInstance() = FlightFragment()
    }

    private lateinit var viewModel: FlightViewModel


    private fun filter(text: String) {
        val filteredList: ArrayList<FlightsResponse> = ArrayList()

        for (item in flightsList) {
            if(isDeparture) {
                if (item.data.arrival.airport.uppercase(Locale.getDefault())
                        .contains(text.uppercase(Locale.getDefault()))
                ) {
                    filteredList.add(item)
                }
            }
            else{
                if (item.data.departure.airport.uppercase(Locale.getDefault())
                        .contains(text.uppercase(Locale.getDefault()))
                ) {
                    filteredList.add(item)
                }
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
        binding = FragmentFlightBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[FlightViewModel::class.java]

        departureButton = binding.departureButton
        arrivalButton = binding.arrivalButton
        flightRecyclerView = binding.flightsRecyclerView
        //viewModel.getDepartureFlights(requireActivity(), getString(R.string.icao))
        //updateFlightsCallback()

        departureButton.setOnClickListener{
            isDeparture=true
            changeButtonStyle(departureButton, arrivalButton)
            viewModel.getDepartureFlights(requireActivity(), getString(R.string.icao))
        }

        arrivalButton.setOnClickListener{
            isDeparture=false
            changeButtonStyle(arrivalButton, departureButton)
            viewModel.getArrivalFlights(requireActivity(), getString(R.string.icao))
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

    private fun updateFlightsCallback() {
        viewModel.flightList.observe(requireActivity()) {
            flightsList = it
            flightsAdapter = FlightsAdapter(flightsList)
            flightRecyclerView.adapter = flightsAdapter
            var position: Int=0
            flightsList.forEach {
                flightsList.get(position).data.airline.airlineNameWithIcon = iconMatching(it.data.airline.name)
                position ++
            }
        }
    }
    private fun iconMatching(flightDate: String) : Drawable {
        if(flightDate.contains("turkish")) {
            return getAsset("turkish_airlines_icon.png")
        }
        if(flightDate.contains("pegasus")) {
            return getAsset("pegasus_airlines_icon.png")
        }
        if(flightDate.contains("azerbaijan")) {
            return getAsset("azerbaijan_airlines_icon.png")
        }
        if(flightDate.contains("aegan")) {
            return getAsset("aegean_airlines_icon.png")
        }
        if(flightDate.contains("freebird")) {
            return getAsset("freebird_airlines_icon.png")
        }
        if(flightDate.contains("qatar")) {
            return getAsset("qatar_airways_icon.png")
        }
        if(flightDate.contains("sunexpress")) {
            return getAsset("sunexpress_icon.png")
        }
        return context?.getDrawable(R.drawable.airplane)!!
    }

    private fun getAsset(s: String) : Drawable {
        try {
            val ims: InputStream? = context?.getAssets()?.open(s)
            val d = Drawable.createFromStream(ims, null)
            return d!!
        } catch (ex: IOException) {
            return context?.getDrawable(R.drawable.airplane)!!
        }
    }

    private fun changeButtonStyle(selected: Button, default: Button) {
        selected.background = ContextCompat.getDrawable(requireContext(), R.drawable.selected_button_background);
        default.background = ContextCompat.getDrawable(requireContext(), R.drawable.default_button_background);
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_tab))
        default.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }
}