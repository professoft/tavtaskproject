package com.professoft.tavtask.repositories

import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.interfaces.NetworkResponseCallback
import com.professoft.tavtask.network.FlightRestClient
import com.professoft.tavtask.utils.FlightsResponse
import com.professoft.tavtask.utils.Keys
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlightRepository private constructor() {
    private lateinit var mutableCallback: NetworkResponseCallback
    private var mutableFlightList: MutableLiveData<FlightsResponse> = MutableLiveData()

    companion object {
        private var mInstance: FlightRepository? = null
        fun getInstance(): FlightRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = FlightRepository()
                }
            }
            return mInstance!!
        }
    }


    private lateinit var mFlightCall: Call<FlightsResponse>

    fun getArrivvalFlights(
        callback: NetworkResponseCallback,
        arr_icao: String,
        offset: String
    ): MutableLiveData<FlightsResponse> {
        mutableCallback = callback

        mFlightCall = FlightRestClient.getInstance().getFlightApiService()
            .getArrivalFlights(Keys.apiKey(), arr_icao, "10", offset)
        mFlightCall.enqueue(object : Callback<FlightsResponse> {

            override fun onResponse(
                call: Call<FlightsResponse>,
                response: Response<FlightsResponse>
            ) {
                mutableFlightList.value = response.body()
                mutableCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<FlightsResponse>, t: Throwable) {
                mutableCallback.onNetworkFailure(t)
            }

        })
        return mutableFlightList
    }

    fun getDepartureFlights(
        callback: NetworkResponseCallback,
        dep_icao: String,
        offset: String
    ): MutableLiveData<FlightsResponse> {
        mutableCallback = callback

        mFlightCall = FlightRestClient.getInstance().getFlightApiService()
            .getDepartureFlights(Keys.apiKey(), dep_icao, "10", offset)
        mFlightCall.enqueue(object : Callback<FlightsResponse> {

            override fun onResponse(
                call: Call<FlightsResponse>,
                response: Response<FlightsResponse>
            ) {
                mutableFlightList.value = response.body()
                mutableCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<FlightsResponse>, t: Throwable) {
                mutableCallback.onNetworkFailure(t)
            }

        })

        return mutableFlightList
    }

}