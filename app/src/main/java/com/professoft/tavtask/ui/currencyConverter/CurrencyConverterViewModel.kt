package com.professoft.tavtask.ui.currencyConverter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.R
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.network.CurrencyRestClient
import com.professoft.tavtask.models.CurrencyResponseModel
import com.professoft.tavtask.utils.NetworkHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal


class CurrencyConverterViewModel() : BaseViewModel() {
    var currenciesList : MutableLiveData<CurrencyResponseModel> = MutableLiveData()

    fun getLatestCurrencies(context: Context) {
        if (NetworkHelper.isOnline(context)) {
            val response =
                CurrencyRestClient.getInstance().getCurrencyApiService().getLatestCurrencies()
            response.enqueue(object : Callback<CurrencyResponseModel> {

                override fun onResponse(
                    call: Call<CurrencyResponseModel>,
                    response: Response<CurrencyResponseModel>
                ) {
                    currenciesList.value = response.body()
                    loading.value = context.getString(R.string.loading_hide_message)
                }

                override fun onFailure(call: Call<CurrencyResponseModel>, t: Throwable) {
                    loading.value = context.getString(R.string.loading_hide_message)
                    networkError.value = t.message
                }

            })
        }
    }
    fun convertCurrency(ratio: BigDecimal, input: BigDecimal) : BigDecimal{
        return ratio.multiply(input)
    }
}