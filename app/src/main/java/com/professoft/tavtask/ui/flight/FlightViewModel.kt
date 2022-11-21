package com.professoft.tavtask.ui.flight

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.data.datastore.DataStoreRepo
import com.professoft.tavtask.interfaces.NetworkResponseCallback
import com.professoft.tavtask.network.FlightRestClient
import com.professoft.tavtask.utils.FlightsResponse
import com.professoft.tavtask.utils.Keys
import com.professoft.tavtask.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(private var datastoreRepo: DataStoreRepo) : BaseViewModel() {
    var flightList: MutableLiveData<FlightsResponse> = MutableLiveData()
    private lateinit var mFlightCall: Call<FlightsResponse>
    private lateinit var mutableCallback: NetworkResponseCallback

    fun getArrivalFlights(context: Context, arr_icao: String) {
        if (NetworkHelper.isOnline(context)) {
            mutableCallback = object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    networkError.value = th.message
                }

                override fun onNetworkSuccess() {
                }
            }
            loading.value = true
            mFlightCall = FlightRestClient.getInstance().getFlightApiService()
                .getArrivalFlights(Keys.apiKey(), arr_icao, "10", getOffset())
            mFlightCall.enqueue(object : Callback<FlightsResponse> {

                override fun onResponse(
                    call: Call<FlightsResponse>,
                    response: Response<FlightsResponse>
                ) {
                    flightList.postValue(response.body())
                    loading.value = false
                    mutableCallback.onNetworkSuccess()
                }

                override fun onFailure(call: Call<FlightsResponse>, t: Throwable) {
                    networkError.value = t.message
                    loading.value = false
                    mutableCallback.onNetworkFailure(t)
                }

            })
        }
    }

    fun getDepartureFlights(context: Context, dep_icao: String) {
        if (NetworkHelper.isOnline(context)) {
            mutableCallback = object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    networkError.value = th.message
                }
                override fun onNetworkSuccess() {
                }
            }
            loading.value = true
            mFlightCall = FlightRestClient.getInstance().getFlightApiService()
                .getDepartureFlights(Keys.apiKey(), dep_icao, "10", getOffset())
            mFlightCall.enqueue(object : Callback<FlightsResponse> {

                override fun onResponse(
                    call: Call<FlightsResponse>,
                    response: Response<FlightsResponse>
                ) {
                    flightList.postValue(response.body())
                    loading.value = false
                    mutableCallback.onNetworkSuccess()
                }

                override fun onFailure(call: Call<FlightsResponse>, t: Throwable) {
                    networkError.value = t.message
                    loading.value = false
                    mutableCallback.onNetworkFailure(t)
                }

            })
        }
    }

     fun setOffset(offset: String) = runBlocking {
        datastoreRepo.putString("offset", offset)
    }

    private fun getOffset(): String = runBlocking {
        datastoreRepo.getString("offset")!!
    }
}