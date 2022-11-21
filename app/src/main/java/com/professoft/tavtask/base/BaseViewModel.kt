package com.professoft.tavtask.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val login: MutableLiveData<Boolean> = MutableLiveData()

    val networkError: MutableLiveData<String> = MutableLiveData()

}