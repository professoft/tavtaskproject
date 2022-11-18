package com.professoft.tavtask.ui.flight

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.interfaces.NetworkResponseCallback
import com.professoft.tavtask.repositories.FlightsRepository
import com.professoft.tavtask.utils.FlightItemsModel
import com.professoft.tavtask.utils.NetworkHelper

class FlightViewModel(private val app: Application) : BaseViewModel() {
    private var mList: MutableLiveData<List<FlightItemsModel>> =
        MutableLiveData<List<FlightItemsModel>>().apply { value = emptyList() }
    private var mRepository = FlightsRepository.getInstance()
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()

    fun fetchArrivalFlightsFromServer(forceFetch: Boolean,arr_iata: String): MutableLiveData<List<FlightItemsModel>> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            loading.value=true
            mList = mRepository.getArrivvalFlights(object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    mShowApiError.value = th.message
                    loading.value=false
                }

                override fun onNetworkSuccess() {
                    loading.value=false
                }
            }, forceFetch,arr_iata)
        } else {
            mShowNetworkError.value = true
        }
        return mList
    }}