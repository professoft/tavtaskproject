package com.professoft.tavtask.utils

class FlightsResponse {
    lateinit var data : List<FlightsModel>
    lateinit var pagination : PaginationModel

    constructor(data: List<FlightsModel>,pagination: PaginationModel) {
        this.data = data
        this.pagination = pagination
    }

    constructor()
}
