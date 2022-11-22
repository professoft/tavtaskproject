package com.professoft.tavtask.ui.flight

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.R
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.data.datastore.DataStoreRepo
import com.professoft.tavtask.network.FlightRestClient
import com.professoft.tavtask.models.FlightResponseModel
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
    var flightList: MutableLiveData<FlightResponseModel> = MutableLiveData()
    private lateinit var mFlightCall: Call<FlightResponseModel>

    fun getArrivalFlights(context: Context, arr_icao: String) {
        if (NetworkHelper.isOnline(context)) {
            loading.value = context.getString(R.string.flight_loading_message)
            mFlightCall = FlightRestClient.getInstance().getFlightApiService()
                .getArrivalFlights(Keys.apiKey(), arr_icao, "10", getOffset())
            mFlightCall.enqueue(object : Callback<FlightResponseModel> {

                override fun onResponse(
                    call: Call<FlightResponseModel>,
                    response: Response<FlightResponseModel>
                ) {
                    flightList.postValue(response.body())
                    loading.value = context.getString(R.string.loading_hide_message)
                }

                override fun onFailure(call: Call<FlightResponseModel>, t: Throwable) {
                    loading.value = context.getString(R.string.loading_hide_message)
                    networkError.value = t.message
                }

            })
        }
    }

    fun getDepartureFlights(context: Context, dep_icao: String) {
        if (NetworkHelper.isOnline(context)) {
            loading.value = context.getString(R.string.flight_loading_message)
            mFlightCall = FlightRestClient.getInstance().getFlightApiService()
                .getDepartureFlights(Keys.apiKey(), dep_icao, "10", getOffset())
            mFlightCall.enqueue(object : Callback<FlightResponseModel> {

                override fun onResponse(
                    call: Call<FlightResponseModel>,
                    response: Response<FlightResponseModel>
                ) {
                    flightList.postValue(response.body())
                    loading.value = context.getString(R.string.loading_hide_message)
                }

                override fun onFailure(call: Call<FlightResponseModel>, t: Throwable) {
                    loading.value = context.getString(R.string.loading_hide_message)
                    networkError.value = t.message
                }

            })
        }
    }

     fun setOffset(offset: String) = runBlocking {
        datastoreRepo.putString("offset", offset)
    }

     fun getOffset(): String = runBlocking {
        datastoreRepo.getString("offset")!!
    }


}