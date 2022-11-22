package com.professoft.tavtask.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.professoft.tavtask.utils.NetworkHelper

open class BaseViewModel : ViewModel() {

    val loading: MutableLiveData<String> = MutableLiveData()

    val login: MutableLiveData<Boolean> = MutableLiveData()

    val networkError: MutableLiveData<String> = MutableLiveData()

    fun isOnline(context: Context): Boolean {
        return NetworkHelper.isOnline(context)
    }
}