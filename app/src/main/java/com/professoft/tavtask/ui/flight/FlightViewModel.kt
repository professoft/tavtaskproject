package com.professoft.tavtask.ui.flight

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.data.datastore.DataStoreRepo
import com.professoft.tavtask.interfaces.NetworkResponseCallback
import com.professoft.tavtask.repositories.FlightRepository
import com.professoft.tavtask.utils.FlightsResponse
import com.professoft.tavtask.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(private var datastoreRepo: DataStoreRepo) : BaseViewModel() {
    var flightResponse: MutableLiveData<FlightsResponse> = MutableLiveData()
    var flightList: MutableLiveData<FlightsResponse> = MutableLiveData()
    private var mRepository = FlightRepository.getInstance()
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()

    fun getArrivalFlights(context: Context, arr_icao: String, offset: String) {
        if (NetworkHelper.isOnline(context)) {
            loading.value = true
            flightResponse = mRepository.getArrivvalFlights(object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    mShowApiError.value = th.message
                    loading.value = false
                }

                override fun onNetworkSuccess() {
                    loading.value = false
                }
            }, arr_icao, offset)
            if (flightResponse.value != null) {
                flightList.postValue(flightResponse.value)
            }
        } else {
            mShowNetworkError.value = true
        }
    }

    fun getDepartureFlights(context: Context, arr_icao: String, offset: String) {
        if (NetworkHelper.isOnline(context)) {
            loading.value = true
            flightResponse = mRepository.getDepartureFlights(object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    mShowApiError.value = th.message
                    loading.value = false
                }

                override fun onNetworkSuccess() {
                    loading.value = false
                }
            }, arr_icao, offset)
            if (flightResponse.value != null) {
                storeOffset(flightResponse.value!!.pagination.offset.toString())
                flightList.postValue(flightResponse.value)
            }
        } else {
            mShowNetworkError.value = true
        }
    }

    fun storeOffset(offset: String) = runBlocking {
        datastoreRepo.putString("offset", offset)
    }

    fun getOffset(): String = runBlocking {
        datastoreRepo.getString("offset")!!
    }
}