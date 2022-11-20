package com.professoft.tavtask.utils

class AirlinesResponse {
    lateinit var data : FlightsModel

    constructor(data: FlightsModel) {
        this.data = data
    }

    constructor()
}
