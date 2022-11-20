package com.professoft.tavtask.repositories

import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.interfaces.NetworkResponseCallback
import com.professoft.tavtask.network.FlightRestClient
import com.professoft.tavtask.utils.AirportsResponse
import com.professoft.tavtask.utils.FlightsResponse
import com.professoft.tavtask.utils.Keys
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlightRepository private constructor() {
    private lateinit var mutableCallback: NetworkResponseCallback
    private var mutableFlightList: MutableLiveData<List<FlightsResponse>> =
        MutableLiveData<List<FlightsResponse>>().apply { value = emptyList() }
    private var mutableAirportList: MutableLiveData<List<AirportsResponse>> =
        MutableLiveData<List<AirportsResponse>>().apply { value = emptyList() }
    private var dap_iata: MutableLiveData<List<String>> =
        MutableLiveData<List<String>>().apply { value = emptyList() }
    private var arr_iata: MutableLiveData<List<FlightsResponse>> =
        MutableLiveData<List<FlightsResponse>>().apply { value = emptyList() }

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


    private lateinit var mFlightCall: Call<List<FlightsResponse>>
    private lateinit var mAirpotCall: Call<List<AirportsResponse>>

    fun getArrivvalFlights(callback: NetworkResponseCallback, arr_icao: String): MutableLiveData<List<FlightsResponse>> {
        mutableCallback = callback
        if (mutableFlightList.value!!.isNotEmpty()) {
            mutableCallback.onNetworkSuccess()
            return mutableFlightList
        }
        mFlightCall = FlightRestClient.getInstance().getFlightApiService().getArrivalFlights(Keys.apiKey(),arr_icao)
        mFlightCall.enqueue(object : Callback<List<FlightsResponse>> {

            override fun onResponse(call: Call<List<FlightsResponse>>, response: Response<List<FlightsResponse>>) {
                mutableFlightList.value = response.body()
                mutableCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<List<FlightsResponse>>, t: Throwable) {
                mutableFlightList.value = emptyList()
                mutableCallback.onNetworkFailure(t)
            }

        })
        return mutableFlightList
    }

    fun getDepartureFlights(callback: NetworkResponseCallback, dep_icao: String): MutableLiveData<List<FlightsResponse>> {
        mutableCallback = callback
        if (mutableFlightList.value!!.isNotEmpty() ) {
            mutableCallback.onNetworkSuccess()
            return mutableFlightList
        }
        mFlightCall = FlightRestClient.getInstance().getFlightApiService().getDepartureFlights(Keys.apiKey(),dep_icao)
        mFlightCall.enqueue(object : Callback<List<FlightsResponse>> {

            override fun onResponse(call: Call<List<FlightsResponse>>, response: Response<List<FlightsResponse>>) {
                mutableFlightList.value = response.body()
                var position: Int = 0
                response.body()?.forEach {
                    var icao_code : String = it.data.departure.icao
                    var airportsResponse : MutableLiveData<List<AirportsResponse>> = getAirportName(callback,icao_code)
                    mutableFlightList.value!!.get(position)!!.data!!.departure!!.airport =
                        airportsResponse.value!!.get(0)!!.data!!.departure!!.airport
                        position++
                }
                mutableCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<List<FlightsResponse>>, t: Throwable) {
                mutableFlightList.value = emptyList()
                mutableCallback.onNetworkFailure(t)
            }

        })

        return mutableFlightList
    }

    fun getAirportName(callback: NetworkResponseCallback, icao_code: String): MutableLiveData<List<AirportsResponse>> {
        mutableCallback = callback
        if (mutableAirportList.value!!.isNotEmpty() ) {
            mutableCallback.onNetworkSuccess()
            return mutableAirportList
        }
        mAirpotCall = FlightRestClient.getInstance().getFlightApiService().getAirportName(Keys.apiKey(),icao_code)
        mAirpotCall.enqueue(object : Callback<List<AirportsResponse>> {

            override fun onResponse(call: Call<List<AirportsResponse>>, response: Response<List<AirportsResponse>>) {
                mutableAirportList.value = response.body()
                mutableCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<List<AirportsResponse>>, t: Throwable) {
                mutableAirportList.value = emptyList()
                mutableCallback.onNetworkFailure(t)
            }

        })
        return mutableAirportList
    }
}