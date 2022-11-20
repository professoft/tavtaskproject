package com.professoft.tavtask.utils

class AirportsResponse {
    lateinit var data : FlightsModel

    constructor(data: FlightsModel) {
        this.data = data
    }

    constructor()
}
