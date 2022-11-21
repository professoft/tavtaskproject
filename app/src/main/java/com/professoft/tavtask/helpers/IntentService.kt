package com.professoft.tavtask.helpers

import android.app.IntentService
import android.content.Intent
import android.os.SystemClock
import com.professoft.point.data.EventBus.CustomMessageEvent

class IntentService: IntentService("IntentService") {

    companion object {
        var loginEvent = MutableLiveData<CustomMessageEvent>()
    }

    override fun onHandleIntent(intent: Intent?) {

        val event = CustomMessageEvent("value")

        if (loginEvent.hasActiveObservers()) {
            loginEvent.postValue(event)
        }
    }
}