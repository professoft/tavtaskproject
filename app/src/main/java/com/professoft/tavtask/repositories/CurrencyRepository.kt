package com.professoft.tavtask.repositories

import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.interfaces.NetworkResponseCallback
import com.professoft.tavtask.network.CurrencyRestClient
import com.professoft.tavtask.utils.CurrencyResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyRepository private constructor() {
    private lateinit var mutableCallback: NetworkResponseCallback

    companion object {
        private var mInstance: CurrencyRepository? = null
        fun getInstance(): CurrencyRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = CurrencyRepository()
                }
            }
            return mInstance!!
        }
    }

    private var mutableCurrencyList: MutableLiveData<CurrencyResponseModel> = MutableLiveData()

    fun getLatestCurrency(callback: NetworkResponseCallback): MutableLiveData<CurrencyResponseModel> {
        mutableCallback = callback


        val response = CurrencyRestClient.getInstance().getCurrencyApiService().getLatestCurrencies()
        response.enqueue(object : Callback<CurrencyResponseModel> {

            override fun onResponse(
                call: Call<CurrencyResponseModel>,
                response: Response<CurrencyResponseModel>
            ) {
                mutableCurrencyList.value = response.body()
                mutableCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<CurrencyResponseModel>, t: Throwable) {
                mutableCallback.onNetworkFailure(t)
            }

        })
        return mutableCurrencyList
    }
}