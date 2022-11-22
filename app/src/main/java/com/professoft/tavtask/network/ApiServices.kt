package com.professoft.tavtask.network

import com.professoft.tavtask.models.CurrencyResponseModel
import com.professoft.tavtask.models.FlightResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("flights")
    fun getDepartureFlights(@Query("access_key") access_key: String,
                            @Query("dep_icao") dep_icao: String,
                            @Query("limit") limit: String,
                            @Query("offset") offset: String) : Call<FlightResponseModel>

    @GET("flights")
    fun getArrivalFlights(@Query("access_key") access_key: String,
                          @Query("arr_icao") arr_icao: String,
                          @Query("limit") limit: String,
                          @Query("offset") offset: String) : Call<FlightResponseModel>

    @GET("gh/fawazahmed0/currency-api@1/latest/currencies/sar.json")
    fun getLatestCurrencies() : Call<CurrencyResponseModel>
}