package com.professoft.tavtask.utils

object Keys {

    init {
        System.loadLibrary("tavtask")
    }

    external fun apiKey(): String
}