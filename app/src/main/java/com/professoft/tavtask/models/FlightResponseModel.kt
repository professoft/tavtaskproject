package com.professoft.tavtask.models

class FlightResponseModel {
    lateinit var data : List<FlightDataModel>
    lateinit var pagination : PaginationModel

    constructor(data: List<FlightDataModel>, pagination: PaginationModel) {
        this.data = data
        this.pagination = pagination
    }

    constructor()
}
