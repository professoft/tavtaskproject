package com.professoft.tavtask.network

import com.professoft.tavtask.utils.FlightItemsModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiServices {

    @GET("/flights")
    fun getDepartureFlights(accessKey: String, dep_iata: String) : Call<List<FlightItemsModel>>

    @GET("/flights")
    fun getArrivalFlights(accessKey: String, arr_iata: String) : Call<List<FlightItemsModel>>

    @GET("/airports")
    fun getDepartureIata(accessKey: String, search: String) : Call<List<String>>

    @GET("/airports")
    fun getArrivalIata(accessKey: String, search: String) : Call<List<String>>
}