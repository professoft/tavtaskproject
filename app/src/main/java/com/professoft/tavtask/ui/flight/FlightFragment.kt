package com.professoft.tavtask.ui.flight

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.professoft.tavtask.R
import com.professoft.tavtask.adapters.FlightsAdapter
import com.professoft.tavtask.databinding.FragmentFlightBinding
import com.professoft.tavtask.utils.FlightsModel
import com.professoft.tavtask.utils.FlightsResponse
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.io.InputStream
import java.util.*


@AndroidEntryPoint
class FlightFragment : Fragment() {
    private var dialog: Dialog? = null
    private lateinit var binding: FragmentFlightBinding
    private lateinit var flightRecyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager

    private lateinit var flightsAdapter: FlightsAdapter
    private lateinit var departureButton: Button
    private lateinit var arrivalButton: Button
    private lateinit var flightsList: FlightsResponse
    private var isDeparture = true;
    private var isLoading = false

    companion object {
        fun newInstance() = FlightFragment()
    }

    private lateinit var viewModel: FlightViewModel


    private fun filter(text: String) {
        val filteredList: ArrayList<FlightsModel> = ArrayList()

        var position = 0
        for (item in flightsList.data) {
            if (isDeparture) {
                if (item.arrival.airport.uppercase(Locale.getDefault())
                        .contains(text.uppercase(Locale.getDefault()))
                ) {
                    filteredList.add(flightsList.data[position])
                }
            } else {
                if (item.departure.airport.uppercase(Locale.getDefault())
                        .contains(text.uppercase(Locale.getDefault()))
                ) {
                    filteredList.add(flightsList.data[position])
                }
            }
            position++
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(activity, "Flight Not Found..", Toast.LENGTH_SHORT).show()
        } else {
            flightsList.data = filteredList
            binding.flightsRecyclerView.adapter!!.notifyDataSetChanged()
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
        manager = LinearLayoutManager(context)
        departureButton = binding.departureButton
        arrivalButton = binding.arrivalButton
        flightRecyclerView = binding.flightsRecyclerView
        viewModel.getDepartureFlights(requireActivity(), getString(R.string.icao),viewModel.getOffset())
        updateFlightsCallback()
        observeLoadingCallback()
        departureButton.setOnClickListener {
            isDeparture = true
            changeButtonStyle(departureButton, arrivalButton)
            viewModel.getDepartureFlights(requireActivity(), getString(R.string.icao),viewModel.getOffset())
        }

        arrivalButton.setOnClickListener {
            isDeparture = false
            changeButtonStyle(arrivalButton, departureButton)
            viewModel.getArrivalFlights(requireActivity(), getString(R.string.icao),viewModel.getOffset())
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
            if (it != null) {
                flightsList = it
                flightsAdapter = FlightsAdapter(flightsList.data)
                flightRecyclerView.adapter = flightsAdapter
                var position: Int = 0
                flightsList.data.forEach {
                    flightsList.data[position].airline.airlineNameWithIcon =
                        iconMatching(it.airline.name)
                    flightsList.data[position].flight.isDeparture = isDeparture
                    position++
                }
                binding.flightsRecyclerView.apply {
                    adapter = FlightsAdapter(flightsList.data)
                    layoutManager = manager
                }
                binding.flightsRecyclerView.adapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun iconMatching(flightDate: String): Drawable {
        if (flightDate.contains("turkish")) {
            return getAsset("turkish_airlines_icon.png")
        }
        if (flightDate.contains("pegasus")) {
            return getAsset("pegasus_airlines_icon.png")
        }
        if (flightDate.contains("azerbaijan")) {
            return getAsset("azerbaijan_airlines_icon.png")
        }
        if (flightDate.contains("aegan")) {
            return getAsset("aegean_airlines_icon.png")
        }
        if (flightDate.contains("freebird")) {
            return getAsset("freebird_airlines_icon.png")
        }
        if (flightDate.contains("qatar")) {
            return getAsset("qatar_airways_icon.png")
        }
        if (flightDate.contains("sunexpress")) {
            return getAsset("sunexpress_icon.png")
        }
        return requireActivity().resources.getDrawable(R.drawable.airplane)!!
    }

    private fun getAsset(s: String): Drawable {
        try {
            val ims: InputStream? = context?.getAssets()?.open(s)
            val d = Drawable.createFromStream(ims, null)
            return d!!
        } catch (ex: IOException) {
            return context?.getDrawable(R.drawable.airplane)!!
        }
    }

    private fun changeButtonStyle(selected: Button, default: Button) {
        selected.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selected_button_background);
        default.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_button_background);
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_tab))
        default.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun observeLoadingCallback() {
        viewModel.loading.observe {
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

    private fun initScrollListener() {
        flightRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == flightsList.data.size - 1) {
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        flightsAdapter.notifyItemInserted(flightsList.data.size - 1)
        val handler = Handler()
        handler.postDelayed(Runnable {
            val scrollPosition: Int = flightsList.data.size
            flightsAdapter.notifyItemRemoved(scrollPosition)
            var currentSize = scrollPosition
            val nextLimit = currentSize + 10
            while (currentSize - 1 < nextLimit) {
                if(isDeparture){
                    viewModel.getDepartureFlights(requireActivity(),getString(R.string.icao),viewModel.getOffset())
                }
                else{
                    viewModel.getArrivalFlights(requireActivity(),getString(R.string.icao),viewModel.getOffset())
                }
            }
            flightsAdapter.notifyDataSetChanged()
            isLoading = false
        }, 2000)
    }


}

private fun <T> MutableLiveData<T>.observe(function: (T) -> Unit) {

}
