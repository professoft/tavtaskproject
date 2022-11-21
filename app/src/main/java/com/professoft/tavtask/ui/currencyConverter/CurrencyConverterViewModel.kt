package com.professoft.tavtask.ui.currencyConverter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.R
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.data.datastore.DataStoreRepo
import com.professoft.tavtask.data.realm.RealmDatabase
import com.professoft.tavtask.interfaces.NetworkResponseCallback
import com.professoft.tavtask.network.CurrencyRestClient
import com.professoft.tavtask.utils.CurrencyResponseModel
import com.professoft.tavtask.utils.NetworkHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.RoundingMode


class CurrencyConverterViewModel() : BaseViewModel() {
    private lateinit var mutableCallback: NetworkResponseCallback
    var currenciesList : MutableLiveData<CurrencyResponseModel> = MutableLiveData()

    fun getLatestCurrencies(context: Context) {
        if (NetworkHelper.isOnline(context)) {
            mutableCallback = object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    networkError.value = th.message
                }

                override fun onNetworkSuccess() {
                }
            }
            loading.value = context.getString(R.string.currency_loading_message)
            val response =
                CurrencyRestClient.getInstance().getCurrencyApiService().getLatestCurrencies()
            response.enqueue(object : Callback<CurrencyResponseModel> {

                override fun onResponse(
                    call: Call<CurrencyResponseModel>,
                    response: Response<CurrencyResponseModel>
                ) {
                    currenciesList.value = response.body()
                    loading.value = context.getString(R.string.loading_hide_message)
                    mutableCallback.onNetworkSuccess()
                }

                override fun onFailure(call: Call<CurrencyResponseModel>, t: Throwable) {
                    networkError.value = t.message
                    loading.value = context.getString(R.string.loading_hide_message)
                    mutableCallback.onNetworkFailure(t)
                }

            })
        }
    }
    fun bigDecimalFormat(bigDecimal: BigDecimal) : BigDecimal{
        return bigDecimal.setScale(2, RoundingMode.HALF_EVEN)
    }
    fun convertCurrency(ratio: BigDecimal, input: BigDecimal) : BigDecimal{
        return bigDecimalFormat(ratio.multiply(input))
    }
}