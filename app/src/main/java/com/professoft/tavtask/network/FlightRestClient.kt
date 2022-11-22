package com.professoft.tavtask.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class FlightRestClient private constructor() {
    companion object {
        private const val FLIGHT_URL = "http://api.aviationstack.com/v1/"
        private lateinit var apiServices: ApiServices
        private var mInstance: FlightRestClient? = null
        fun getInstance(): FlightRestClient {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = FlightRestClient()
                }
            }
            return mInstance!!
        }
    }

    init {
        val okHttpClient = OkHttpClient().newBuilder().connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder().baseUrl(FLIGHT_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiServices = retrofit.create(ApiServices::class.java)
    }


    fun getFlightApiService() = apiServices

}