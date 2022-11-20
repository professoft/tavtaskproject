package com.professoft.tavtask.utils

class FlightsResponse {
    lateinit var data : List<FlightsModel>

    constructor(data: List<FlightsModel>) {
        this.data = data
    }

    constructor()
}
