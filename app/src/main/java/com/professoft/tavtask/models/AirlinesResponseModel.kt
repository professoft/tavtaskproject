package com.professoft.tavtask.models

class AirlinesResponseModel {
    lateinit var data : FlightDataModel

    constructor(data: FlightDataModel) {
        this.data = data
    }

    constructor()
}
