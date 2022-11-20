package com.professoft.tavtask.ui.currencyConverter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.interfaces.NetworkResponseCallback
import com.professoft.tavtask.repositories.CurrencyRepository
import com.professoft.tavtask.utils.CurrencyConverterItemModel
import com.professoft.tavtask.utils.CurrencyResponseModel
import com.professoft.tavtask.utils.NetworkHelper
import java.math.BigDecimal


class CurrencyConverterViewModel : BaseViewModel() {
    var currenciesList : MutableLiveData<CurrencyResponseModel> = MutableLiveData()
    private var mRepository = CurrencyRepository.getInstance()

    fun getLatestCurrencies(context: Context){
        if (NetworkHelper.isOnline(context)) {
            loading.postValue(true)
            val response = mRepository.getLatestCurrency(object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    loading.postValue(false)
                }

                override fun onNetworkSuccess() {
                    loading.postValue(false)
                }
            })
            if(response.value != null) {
                currenciesList.postValue(response.value)
            }
        } else {
            //Network is offline
        }
    }

}