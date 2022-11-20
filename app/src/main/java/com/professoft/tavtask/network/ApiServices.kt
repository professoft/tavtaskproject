package com.professoft.tavtask.network

import com.professoft.tavtask.utils.AirportsResponse
import com.professoft.tavtask.utils.CurrencyResponseModel
import com.professoft.tavtask.utils.FlightsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("flights")
    fun getDepartureFlights(@Query("access_key") access_key: String, @Query("dep_icao") dep_icao: String) : Call<List<FlightsResponse>>

    @GET("flights")
    fun getArrivalFlights(@Query("access_key") access_key: String, @Query("arr_icao") arr_icao: String) : Call<List<FlightsResponse>>

    @GET("airports")
    fun getAirportName(@Query("access_key") access_key: String, @Query("search") search: String) : Call<List<AirportsResponse>>

    @GET("gh/fawazahmed0/currency-api@1/latest/currencies/sar.json")
    fun getLatestCurrencies() : Call<CurrencyResponseModel>
}