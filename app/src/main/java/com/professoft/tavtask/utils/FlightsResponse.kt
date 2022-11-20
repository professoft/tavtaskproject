package com.professoft.tavtask.utils

class FlightsResponse {
    lateinit var data : FlightsModel

    constructor(data: FlightsModel) {
        this.data = data
    }

    constructor()
}
