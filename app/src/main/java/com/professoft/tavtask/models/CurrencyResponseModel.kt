package com.professoft.tavtask.models

import java.math.BigDecimal

class CurrencyResponseModel {
        lateinit var date: String
        lateinit var sar:LinkedHashMap<String, BigDecimal>

    constructor(date: String, sar: LinkedHashMap<String, BigDecimal>) {
            this.date = date
            this.sar = sar
        }

        constructor()
    }
