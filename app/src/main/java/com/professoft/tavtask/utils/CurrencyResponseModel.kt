package com.professoft.tavtask.utils

import androidx.lifecycle.MutableLiveData
import java.math.BigDecimal

class CurrencyResponseModel {
        lateinit var date: String
        lateinit var sar:HashMap<String, BigDecimal>

    constructor(date: String, sar: HashMap<String, BigDecimal>) {
            this.date = date
            this.sar = sar
        }

        constructor()
    }
