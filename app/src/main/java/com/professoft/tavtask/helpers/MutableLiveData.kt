package com.professoft.tavtask.helpers

import androidx.lifecycle.LiveData


class MutableLiveData<T> : LiveData<T>() {
    public override fun postValue(value: T) {
        super.postValue(value)
    }

    public override fun setValue(value: T) {
        super.setValue(value)
    }
}