package com.professoft.tavtask.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CurrencyRestClient {
    companion object {
        private const val CURRENCY_URL = "https://cdn.jsdelivr.net/"
        private lateinit var apiServices: ApiServices
        private var mInstance: CurrencyRestClient? = null

        fun getInstance(): CurrencyRestClient {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = CurrencyRestClient()
                }
            }
            return mInstance!!
        }
    }
    init {
        val okHttpClient = OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder().baseUrl(CURRENCY_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiServices = retrofit.create(ApiServices::class.java)
    }
    fun getCurrencyApiService() = apiServices
}