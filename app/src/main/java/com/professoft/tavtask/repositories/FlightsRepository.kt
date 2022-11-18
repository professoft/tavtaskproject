package com.professoft.tavtask.repositories

import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.interfaces.NetworkResponseCallback
import com.professoft.tavtask.network.RestClient
import com.professoft.tavtask.utils.FlightItemsModel
import com.professoft.tavtask.utils.Keys
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlightsRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback
    private var mFlightList: MutableLiveData<List<FlightItemsModel>> =
        MutableLiveData<List<FlightItemsModel>>().apply { value = emptyList() }
    private var dap_iata: MutableLiveData<List<String>> =
        MutableLiveData<List<String>>().apply { value = emptyList() }
    private var arr_iata: MutableLiveData<List<String>> =
        MutableLiveData<List<String>>().apply { value = emptyList() }

    companion object {
        private var mInstance: FlightsRepository? = null
        fun getInstance(): FlightsRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = FlightsRepository()
                }
            }
            return mInstance!!
        }
    }


    private lateinit var mFlightCall: Call<List<FlightItemsModel>>
    private lateinit var mIataCall: Call<List<String>>

    fun getArrivvalFlights(callback: NetworkResponseCallback, forceFetch : Boolean, arr_iata: String): MutableLiveData<List<FlightItemsModel>> {
        mCallback = callback
        if (mFlightList.value!!.isNotEmpty() && !forceFetch) {
            mCallback.onNetworkSuccess()
            return mFlightList
        }
        mFlightCall = RestClient.getInstance().getApiService().getArrivalFlights(Keys.apiKey(),arr_iata)
        mFlightCall.enqueue(object : Callback<List<FlightItemsModel>> {

            override fun onResponse(call: Call<List<FlightItemsModel>>, response: Response<List<FlightItemsModel>>) {
                mFlightList.value = response.body()
                mCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<List<FlightItemsModel>>, t: Throwable) {
                mFlightList.value = emptyList()
                mCallback.onNetworkFailure(t)
            }

        })
        return mFlightList
    }
    fun getDepartureFlights(callback: NetworkResponseCallback, forceFetch : Boolean,dep_iata: String): MutableLiveData<List<FlightItemsModel>> {
        mCallback = callback
        if (mFlightList.value!!.isNotEmpty() && !forceFetch) {
            mCallback.onNetworkSuccess()
            return mFlightList
        }
        mFlightCall = RestClient.getInstance().getApiService().getDepartureFlights(Keys.apiKey(),dep_iata)
        mFlightCall.enqueue(object : Callback<List<FlightItemsModel>> {

            override fun onResponse(call: Call<List<FlightItemsModel>>, response: Response<List<FlightItemsModel>>) {
                mFlightList.value = response.body()
                mCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<List<FlightItemsModel>>, t: Throwable) {
                mFlightList.value = emptyList()
                mCallback.onNetworkFailure(t)
            }

        })
        return mFlightList
    }
    fun getArrivalIata(callback: NetworkResponseCallback, forceFetch : Boolean,airport_name: String): MutableLiveData<List<String>> {
        mCallback = callback
        if (arr_iata.value!!.isNotEmpty() && !forceFetch) {
            mCallback.onNetworkSuccess()
            return arr_iata
        }
        mIataCall = RestClient.getInstance().getApiService().getArrivalIata(Keys.apiKey(),airport_name)
        mIataCall.enqueue(object : Callback<List<String>> {

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                arr_iata.value = response.body()
                mCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                arr_iata.value = emptyList()
                mCallback.onNetworkFailure(t)
            }

        })
        return arr_iata
    }
    fun getDepartureIata(callback: NetworkResponseCallback, forceFetch : Boolean,airport_name: String): MutableLiveData<List<String>> {
        mCallback = callback
        if (arr_iata.value!!.isNotEmpty() && !forceFetch) {
            mCallback.onNetworkSuccess()
            return arr_iata
        }
        mIataCall = RestClient.getInstance().getApiService().getDepartureIata(Keys.apiKey(),airport_name)
        mIataCall.enqueue(object : Callback<List<String>> {

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                arr_iata.value = response.body()
                mCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                arr_iata.value = emptyList()
                mCallback.onNetworkFailure(t)
            }

        })
        return arr_iata
    }
}