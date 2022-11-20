package com.professoft.tavtask.ui.flight

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.interfaces.NetworkResponseCallback
import com.professoft.tavtask.repositories.FlightRepository
import com.professoft.tavtask.utils.FlightsResponse
import com.professoft.tavtask.utils.NetworkHelper


class FlightViewModel : BaseViewModel() {
    var flightList: MutableLiveData<List<FlightsResponse>> =
        MutableLiveData<List<FlightsResponse>>().apply { value = emptyList() }
    private var mRepository = FlightRepository.getInstance()
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()

    fun getArrivalFlights(context: Context,arr_icao: String): MutableLiveData<List<FlightsResponse>> {
        if (NetworkHelper.isOnline(context)) {
            loading.value=true
            flightList = mRepository.getArrivvalFlights(object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    mShowApiError.value = th.message
                    loading.value=false
                }

                override fun onNetworkSuccess() {
                    loading.value=false
                }
            }, arr_icao)
        } else {
            mShowNetworkError.value = true
        }
        return flightList
    }
    fun getDepartureFlights(context: Context,arr_icao: String): MutableLiveData<List<FlightsResponse>> {
        if (NetworkHelper.isOnline(context)) {
            loading.value=true
            flightList = mRepository.getDepartureFlights(object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    mShowApiError.value = th.message
                    loading.value=false
                }

                override fun onNetworkSuccess() {
                    loading.value=false
                }
            }, arr_icao)
        } else {
            mShowNetworkError.value = true
        }
        return flightList
    }
}